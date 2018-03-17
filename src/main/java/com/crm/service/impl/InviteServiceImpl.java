package com.crm.service.impl;

import com.crm.config.CommonConfig;
import com.crm.constant.CommonConstant;
import com.crm.entity.Group;
import com.crm.entity.GroupMember;
import com.crm.entity.Member;
import com.crm.entity.Point;
import com.crm.repository.GroupMemberRepository;
import com.crm.repository.GroupRepository;
import com.crm.repository.MemberRepository;
import com.crm.repository.PointRepository;
import com.crm.service.InviteService;
import com.crm.util.SnowflakeGenerator;
import com.crm.vo.MemberVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wlrllr on 2018/3/14.
 */
@Service
public class InviteServiceImpl implements InviteService {


    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private CommonConfig commonConfig;

    private static final Logger logger = LoggerFactory.getLogger(InviteServiceImpl.class);

    /**
     * 邀请用户 1新增用户
     *
     */
    @Override
    public boolean qualifying(Member member) {
        addNewMember(member);
        //根据邀请人查找其分组
        return qualifying(member.getMemberId(),member.getParentId());
    }

    /**
     * 添加顶级会员
     * @param vo
     * @return
     */
    @Override
    public boolean crmOpen(MemberVo vo) {
        Member member = vo.getMember();
        addNewMember(member);
        addGroup(member.getMemberId());
        return true;
    }

    /**
     * 邀请用户 2，通过会员，邀请人对会员进行分组排位
     * @param memberId
     * @param parentId
     * @return
     */
    private boolean qualifying(Long memberId, Long parentId) {
        //根据邀请人查找其分组
        List<Group> list = groupRepository.findGroupByMemberId(parentId);
        if (list != null && list.size() > 0) {
            Group group = list.get(0);
            return qualifying(memberId, group);
        }
        return false;
    }

    /**
     * 新增会员信息
     * @param member
     */
    private void addNewMember(Member member) {
        member.setMemberId(SnowflakeGenerator.generator());
        memberRepository.save(member);
        //新增会员积分
        Point point = new Point();
        point.setPointId(SnowflakeGenerator.generator());
        point.setMemberId(member.getMemberId());
        point.setInitPoint(commonConfig.getInitPoint());
        pointRepository.save(point);
    }

    /**
     * 新增会员信息
     * @param memberId
     */
    private void addGroup(Long memberId) {
        Group group = new Group();
        group.setStatus(CommonConstant.GROUP_STATUS_Y);
        group.setCreateTime(new Date());
        group.setGroupSize(commonConfig.getGroupSize());
        group.setCurrentSize(1);
        group.setGroupId(SnowflakeGenerator.generator());
        group.setName(group.getGroupId().toString());
        groupRepository.save(group);
        groupMemberRepository.save(new GroupMember(memberId, group.getGroupId(), 1));
    }

    /**
     * 邀请用户 3，将会员分配到该组，然后判断该组是否满员
     * @param memberId
     * @param group
     * @return
     */
    private boolean qualifying(Long memberId, Group group) {
        if (group != null) {
            //排位
            if (qualifying7(memberId, group)) {
                group.setCurrentSize(group.getCurrentSize() + 1);
                groupRepository.updateCurrentSize(group.getCurrentSize(), group.getGroupId());
            }
            //判断该分组是否已满，如果满了进行分组
            if (group.getGroupSize().intValue() == group.getCurrentSize().intValue()) {
                //进行组拆分，配送积分及money，并重新获取新的分组
                deMerge(group);
            }
        }
        return true;
    }

    /**
     * 邀请用户 4，该组已满员，对会员进行奖励，对出局人重新分组，拆分成2组
     * @param group
     */
    private void deMerge(Group group) {
        //设置该组已经满员
        groupRepository.filledGroup(new Date(), group.getGroupId());
        //查询位置为1的用户，并使其出局
        Member member = memberRepository.find1ByGroupId(group.getGroupId());
        reward(member);
        if (member.getParentId() != null) {
            qualifying(member.getMemberId(), member.getParentId());
        } else {
            //这里处理顶级会员
            //查询最近可用的组，将顶级会员分配到该组
            List<Group> groups = groupRepository.findLastGroup();
            if (groups != null && groups.size() > 0) {
                qualifying(member.getMemberId(), groups.get(0));
            }
        }
        //分成两小组245一组，367一组
        deMergeGroup(group.getGroupId());
    }

    /**
     * 邀请用户 4-1，奖励出局人及其推荐者
     * @param member
     */
    private void reward(Member member){
       if(member != null){
           pointRepository.addPoint(member.getMemberId(),commonConfig.getRewardMember());
           if(member.getParentId() != null){
               pointRepository.addPoint(member.getParentId(),commonConfig.getRewardParent());
           }
       }
    }

    /**
     * 邀请用户 4-2，拆分两组
     *
     * @param groupId
     */
    private void deMergeGroup(Long groupId) {
        List<GroupMember> list = groupMemberRepository.findByGroupId(groupId);
        List<Map<Integer, Integer>> mergeGroupStrategys = CommonConstant.deMergeGroup;

        List<GroupMember> groupMembers = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        //分组策略
        for (Map<Integer, Integer> strategy : mergeGroupStrategys) {
            //循环去执行每个分组策略
            Group group = new Group();
            group.setStatus(CommonConstant.GROUP_STATUS_Y);
            group.setCreateTime(new Date());
            group.setGroupSize(commonConfig.getGroupSize());
            group.setCurrentSize(0);
            group.setName(group.getGroupId().toString());
            group.setGroupId(SnowflakeGenerator.generator());
            groups.add(group);
            //建议新组与会员之间关系
            for (Map.Entry<Integer, Integer> groupStrategy : strategy.entrySet()) {
                for (GroupMember gm : list) {
                    //该位置的用户分到新组
                    if (groupStrategy.getValue().intValue() == gm.getPosition()) {
                        groupMembers.add(new GroupMember(gm.getMemberId(), group.getGroupId(), groupStrategy.getKey()));
                        group.setCurrentSize(group.getCurrentSize() + 1);
                    }
                }
            }
        }
        groupRepository.save(groups);
        groupMemberRepository.save(groupMembers);
    }

    /**
     * 邀请用户 3-1，对会员进行排位
     * 按7人组策略排位
     * @param memberId
     * @param group
     * @return
     */
    private boolean qualifying7(Long memberId, Group group) {
        //获取邀请人的当前位置
        Integer position = group.getPosition();
        //FIXME 获取未占的位置点
        List<Integer> unUserPosition = new ArrayList<>();
        //通过策略去查找最优情况
        Map<String, Integer[]> rankStrategy = commonConfig.getRankStrategy();
        Integer[] strategy = rankStrategy.get(String.valueOf(position));
        int newPosition = 0;
        for (Integer s : strategy) {
            if (unUserPosition.contains(s)) {
                newPosition = Integer.valueOf(s);
            }
        }
        if (newPosition != 0) {
            GroupMember groupMember = new GroupMember();
            groupMember.setGroupId(group.getGroupId());
            groupMember.setMemberId(memberId);
            groupMember.setPosition(newPosition);
            groupMemberRepository.save(groupMember);
            groupRepository.updateCurrentSize(group.getCurrentSize() + 1, group.getGroupId());
            return true;
        } else {
            logger.error("用户排位失败，邀请人【{}】，会员【{}】，组【{}】", group.getMemberId(), memberId, group.getGroupId());
            return false;
        }

    }
}
