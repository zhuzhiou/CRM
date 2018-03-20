package club.starcard.modules.member.service.impl;

import club.starcard.config.CommonConfig;
import club.starcard.modules.member.constant.CommonConstant;
import club.starcard.modules.member.entity.Group;
import club.starcard.modules.member.entity.GroupMember;
import club.starcard.modules.member.entity.Member;
import club.starcard.modules.member.entity.PointLog;
import club.starcard.modules.member.repository.GroupMemberRepository;
import club.starcard.modules.member.repository.GroupRepository;
import club.starcard.modules.member.repository.MemberRepository;
import club.starcard.modules.member.repository.PointLogRepository;
import club.starcard.modules.member.service.GroupService;
import club.starcard.modules.member.service.InviteService;
import club.starcard.modules.member.service.MemberService;
import club.starcard.util.SnowflakeGenerator;
import com.alibaba.fastjson.JSONObject;
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
    private MemberService memberService;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private PointLogRepository pointLogRepository;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private GroupService groupService;

    private static final Logger logger = LoggerFactory.getLogger(InviteServiceImpl.class);

    /**
     * 邀请用户 1新增用户
     */
    @Override
    public boolean qualifying(Member member) {
        if (!addNewMember(member, true)) {
            logger.error("邀请用户时，没有邀请人或者邀请人不存在!");
            return false;
        }
        //根据邀请人查找其分组
        return qualifying(member.getMemberId(), member.getParentId());
    }

    /**
     * 添加顶级会员
     *
     * @param member
     * @return
     */
    @Override
    public boolean crmOpen(Member member) {
        if (addNewMember(member, false)) {
            return addGroup(member.getMemberId());
        }
        return false;
    }

    /**
     * 邀请用户 2，通过会员，邀请人对会员进行分组排位
     *
     * @param memberId
     * @param parentId
     * @return
     */
    private boolean qualifying(Long memberId, Long parentId) {
        //根据邀请人查找其分组
        List<Group> list = groupService.findGroupByMemberId(parentId);
        if (list != null && list.size() > 0) {
            Group group = list.get(0);
            return qualifying(memberId, group);
        }
        return false;
    }

    /**
     * 新增会员信息
     *
     * @param member
     */
    private boolean addNewMember(Member member, Boolean hasParentId) {
        member.setMemberId(SnowflakeGenerator.generator());
        if (hasParentId) {
            Long parentId = member.getParentId();
            if (parentId != null) {
                Member parent = memberRepository.findOne(member.getParentId());
                if (parent != null) {
                    member.setParentName(parent.getName());
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        member.setCreateTime(new Date());
        member.setTotalPoint(commonConfig.getInitPoint());
        member.setUsablePoint(commonConfig.getInitPoint());
        memberRepository.save(member);
        //新增积分变更记录
        PointLog log = new PointLog();
        log.setId(SnowflakeGenerator.generator());
        log.setMemberId(member.getMemberId());
        log.setTotalPoint(member.getTotalPoint());
        log.setOperatePoint(member.getTotalPoint());
        log.setOperateType(CommonConstant.POINT_OPERATE_ADD);
        log.setCreateTime(new Date());
        log.setRemark("新入会原初始积分");
        pointLogRepository.save(log);
        return true;
    }

    /**
     * 新增会员信息
     *
     * @param memberId
     */
    private boolean addGroup(Long memberId) {
        Group group = new Group();
        group.setStatus(CommonConstant.GROUP_STATUS_Y);
        group.setCreateTime(new Date());
        group.setGroupSize(commonConfig.getGroupSize());
        group.setCurrentSize(1);
        group.setGroupId(SnowflakeGenerator.generator());
        group.setName(group.getGroupId().toString());
        groupRepository.save(group);
        groupMemberRepository.save(new GroupMember(memberId, group.getGroupId(), 1));
        return true;
    }

    /**
     * 邀请用户 3，将会员分配到该组，然后判断该组是否满员
     *
     * @param memberId
     * @param group
     * @return
     */
    private boolean qualifying(Long memberId, Group group) {
        if (group != null) {
            //排位
            qualifying7(memberId, group);
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
     *
     * @param group
     */
    private void deMerge(Group group) {
        //设置该组已经满员
        groupRepository.filledGroup(new Date(), group.getGroupId());
        //查询位置为1的用户，并使其出局
        Member member = memberService.find1ByGroupId(group.getGroupId());
        if(member != null){
            reward(member);
            if (member.getParentId() != null) {
                qualifying(member.getMemberId(), member.getParentId());
            } else {
                //这里处理顶级会员
                //查询最近可用的组，将顶级会员分配到该组
                List<Group> groups = groupService.findLastGroup();
                if (groups != null && groups.size() > 0) {
                    qualifying(member.getMemberId(), groups.get(0));
                }
            }
            //分成两小组245一组，367一组
            deMergeGroup(group.getGroupId());
        }
    }

    /**
     * 邀请用户 4-1，奖励出局人及其推荐者
     *
     * @param member
     */
    private void reward(Member member) {
        if (member != null) {
            memberService.rewardMember(member.getMemberId(),member.getTotalPoint(),commonConfig.getRewardMember(),"满组奖励");
            if (member.getParentId() != null) {
                memberService.rewardMember(member.getParentId(),member.getTotalPoint(),commonConfig.getRewardParent(),"满组奖励");
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
     *
     * @param memberId
     * @param group
     * @return
     */
    private boolean qualifying7(Long memberId, Group group) {
        //获取邀请人的当前位置
        Integer position = group.getPosition();
        List<Integer> unUserPosition = commonConfig.getGroupPosition();
        //查询所有已占位置
        if(group.getGroupId() != null){
            List<GroupMember> list = groupMemberRepository.findByGroupId(group.getGroupId());
            if(list != null && list.size()>0){
               for(GroupMember gm : list){
                   if(unUserPosition.contains(gm.getPosition())){
                       unUserPosition.remove(gm.getPosition());
                   }
               }
            }
            logger.info("用户排位，组内空闲位置【{}】,组【{}】", JSONObject.toJSONString(unUserPosition),group.getGroupId());
        }else{
            logger.error("用户排位失败，组ID为空，邀请人【{}】，会员【{}】", group.getMemberId(), memberId);
            return false;
        }

        //通过策略去查找最优情况
        Map<String, Integer[]> rankStrategy = commonConfig.getRankStrategy();
        Integer[] strategy = rankStrategy.get(String.valueOf(position));
        int newPosition = 0;
        for (Integer s : strategy) {
            if (unUserPosition.contains(s)) {
                newPosition = Integer.valueOf(s);
                break;
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
