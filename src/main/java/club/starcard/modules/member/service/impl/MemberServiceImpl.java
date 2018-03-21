package club.starcard.modules.member.service.impl;

import club.starcard.config.CommonConfig;
import club.starcard.modules.member.constant.CommonConstant;
import club.starcard.modules.member.entity.Group;
import club.starcard.modules.member.entity.Member;
import club.starcard.modules.member.entity.PointLog;
import club.starcard.modules.member.repository.MemberRepository;
import club.starcard.modules.member.repository.PointLogRepository;
import club.starcard.modules.member.service.MemberService;
import club.starcard.modules.member.specification.CrmSpecification;
import club.starcard.util.SnowflakeGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wlrllr on 2018/3/14.
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository repository;
    @Autowired
    private PointLogRepository pointLogRepository;
    @Autowired
    private CommonConfig commonConfig;

    @Override
    public Page<Member> page(Date beginDate, Date endDate, String search, Integer pageNum, Integer pageSize) {
        if (pageSize == null) {
            pageSize = CommonConstant.pageSize;
        }
        //pageable从0页开始
        Pageable pageable = new PageRequest(pageNum - 1, pageSize);
        return repository.findAll(CrmSpecification.queryMemberSpecification(beginDate, endDate, search), pageable);
    }

    @Override
    public Page<Member> page(Member member, Pageable pageable) {
        return repository.findAll(Example.of(member),pageable);
    }

    @Override
    public boolean updateSelection(Member member) {
        if (member == null || member.getMemberId() == null) {
            return false;
        }
        Member m = repository.getOne(member.getMemberId());
        if (StringUtils.isNotBlank(member.getIdCard()))
            m.setIdCard(member.getIdCard());
        if (StringUtils.isNotBlank(member.getAddress()))
            m.setAddress(member.getAddress());
        if (StringUtils.isNotBlank(member.getRemark()))
            m.setRemark(member.getRemark());
        if (StringUtils.isNotBlank(member.getPhone()))
            m.setPhone(member.getPhone());
        if (StringUtils.isNotBlank(member.getBankAccount()))
            m.setBankAccount(member.getBankAccount());
        if(StringUtils.isNotBlank(member.getSex()))
            m.setSex(member.getSex());
        if(StringUtils.isNotBlank(member.getNickName()))
            m.setNickName(member.getNickName());
        if (StringUtils.isNotBlank(member.getBankCode())){
            m.setBankCode(member.getBankCode());
            member.setBankName(commonConfig.getBankName(member.getBankCode()));
        }
        repository.save(m);
        return true;
    }

    @Override
    public Member get(Long memberId) {
        if(memberId == null){
            return new Member();
        }
        return repository.findOne(memberId);
    }

    @Override
    public void rewardMember(Long memberId,Long totalPoint,Long rewardPoint,String remark) {
        repository.addPoint(memberId, rewardPoint);
        PointLog log = new PointLog();
        log.setId(SnowflakeGenerator.generator());
        log.setMemberId(memberId);
        log.setTotalPoint(totalPoint+rewardPoint);
        log.setOperatePoint(rewardPoint);
        log.setOperateType(CommonConstant.POINT_OPERATE_ADD);
        log.setCreateTime(new Date());
        log.setRemark(remark);
        pointLogRepository.save(log);
    }

    @Override
    public Member find1ByGroupId(Long groupId) {
        List<Member> list = covertData(repository.findByGroupId(groupId));
        if(list != null && list.size()>0){
            for(Member member : list){
                Integer position = member.getPosition();
                if(position != null && position.intValue() == 1){
                    return member;
                }
            }
        }
        return null;
    }

    @Override
    public List<Member> findByGroupId(Long groupId) {
        return covertData(repository.findByGroupId(groupId));
    }

    private List<Member> covertData(List<Object[]> data){
        List<Member> members = new ArrayList<>();
        if(data != null && data.size()>0){
            for(Object[] obj : data){
                Member member = new Member();
                members.add(member);
                if(obj[0] instanceof BigInteger){
                    member.setMemberId(((BigInteger)obj[0]).longValue());
                }else{
                    member.setMemberId(((Long)obj[0]));
                }
                if(obj[1] instanceof BigInteger){
                    member.setParentId(((BigInteger)obj[1]).longValue());
                }else{
                    member.setParentId(((Long)obj[1]));
                }
                member.setTotalPoint((Long)obj[2]);
                member.setUsablePoint((Long)obj[3]);
                member.setName((String)obj[4]);
                member.setParentName((String)obj[5]);
                member.setAddress((String)obj[6]);
                member.setPhone((String)obj[7]);
                member.setRemark((String)obj[8]);
                member.setPosition((Integer) obj[9]);
            }
            return members;
        }
        return members;
    }

    @Override
    public Page<PointLog> pagePointLog(Long memberId,Integer pageNum, Integer pageSize){
        if (pageSize == null) {
            pageSize = CommonConstant.pageSize;
        }
        Pageable pageable = new PageRequest(pageNum - 1, pageSize);
        PointLog log = new PointLog();
        log.setMemberId(memberId);
        return pointLogRepository.findAll(Example.of(log),pageable);
    }

    @Override
    public List<Member> findByParentId(Long parentId) {
        return repository.findByParentId(parentId);
    }

    @Override
    public List<PointLog> findPointLogs(Long memberId) {
        return pointLogRepository.findByMemberId(memberId);
    }
}
