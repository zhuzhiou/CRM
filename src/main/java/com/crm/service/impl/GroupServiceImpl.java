package com.crm.service.impl;

import com.crm.config.CommonConfig;
import com.crm.constant.CommonConstant;
import com.crm.entity.Group;
import com.crm.entity.GroupMember;
import com.crm.entity.Member;
import com.crm.repository.GroupMemberRepository;
import com.crm.repository.GroupRepository;
import com.crm.repository.MemberRepository;
import com.crm.service.GroupService;
import com.crm.util.SnowflakeGenerator;
import com.crm.vo.GroupMemberVo;
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
public class GroupServiceImpl implements GroupService {


    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private CommonConfig commonConfig;

    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    /**
     * 受邀用户排位
     *
     * @param invitedId 受邀人
     * @param inviterId 邀请人
     */
    @Override
    public void qualifying(Long invitedId, Long inviterId) {
        //根据邀请人查找其分组
        List<GroupMemberVo> list = groupRepository.findGroupByMemberId(inviterId);
        if (list != null && list.size() > 0) {
            GroupMemberVo groupMemberVo = list.get(0);
            if (groupMemberVo.getGroupSize() - groupMemberVo.getCurrentSize() == 1) {
                //进行组拆分，配送积分及money，并重新获取新的分组
                deMerge(invitedId, groupMemberVo);
            } else if (qualifying7(invitedId, groupMemberVo)) { //排位
                Group group = groupRepository.findOne(groupMemberVo.getGroupId());
                group.setCurrentSize(group.getCurrentSize() + 1);
                groupRepository.save(group);
            }
        }
    }

    private void deMerge(Long invitedId, GroupMemberVo groupMemberVo) {
        //给该组添加最后一个位置，然后设置该组已经满员
        if (qualifying7(invitedId, groupMemberVo)) {
            Group group = groupRepository.findOne(groupMemberVo.getGroupId());
            group.setCurrentSize(group.getCurrentSize() + 1);
            group.setStatus(CommonConstant.GROUP_STATUS_N);
            groupRepository.save(group);
        }
        //给组内用户奖励

        //给位置一用户排位
        Member member = memberRepository.find1ByGroupId(groupMemberVo.getGroupId());
        if (member.getInviter() != null) {
            qualifying(member.getId(), member.getInviter());
        } else {
            //FIXME 这里处理顶级会员
        }
        //分成两小组245一组，367一组
        deMergeGroup(groupMemberVo.getGroupId());

    }

    /**
     * 拆分成两个新组
     *
     * @param groupId
     */
    private void deMergeGroup(Long groupId) {
        List<GroupMember> list = groupMemberRepository.findByGroupId(groupId);
        List<Map<Integer, Integer>> mergeGroupStrategys = commonConfig.getDeMergeGroup();

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
            group.setId(SnowflakeGenerator.generator());
            groups.add(group);
            //建议新组与会员之间关系
            for (Map.Entry<Integer, Integer> groupStrategy : strategy.entrySet()) {
                for (GroupMember gm : list) {
                    //该位置的用户分到新组
                    if (groupStrategy.getValue().intValue() == gm.getPosition()) {
                        groupMembers.add(new GroupMember(gm.getMemberId(), group.getId(), groupStrategy.getKey()));
                        group.setCurrentSize(group.getCurrentSize()+1);
                    }
                }
            }
        }
        groupRepository.save(groups);
        groupMemberRepository.save(groupMembers);
    }

    private boolean qualifying7(Long invitedId, GroupMemberVo groupMemberVo) {
        //获取邀请人的当前位置
        Integer position = groupMemberVo.getPosition();
        //获取未占的位置点
        List<String> unUserPosition = new ArrayList<>();
        //通过策略去查找最优情况
        Map<Integer, String[]> rankStrategy = commonConfig.getRankStrategy();
        String[] strategy = rankStrategy.get(position);
        int newPosition = 0;
        for (String s : strategy) {
            if (unUserPosition.contains(s)) {
                newPosition = Integer.valueOf(s);
            }
        }
        if (newPosition != 0) {
            GroupMember groupMember = new GroupMember();
            groupMember.setGroupId(groupMemberVo.getGroupId());
            groupMember.setMemberId(invitedId);
            groupMember.setPosition(newPosition);
            groupMemberRepository.save(groupMember);
            Group group = groupRepository.findOne(groupMemberVo.getGroupId());
            group.setCurrentSize(group.getCurrentSize() + 1);
            groupRepository.save(group);
            return true;
        } else {
            logger.error("用户排位失败，邀请人【{}】，受邀人【{}】，组【{}】", groupMemberVo.getMemberId(), invitedId, groupMemberVo.getGroupId());
            return false;
        }

    }
}
