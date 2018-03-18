package club.starcard.modules.member.service.impl;

import club.starcard.modules.member.constant.CommonConstant;
import club.starcard.modules.member.entity.Member;
import club.starcard.modules.member.repository.MemberRepository;
import club.starcard.modules.member.service.MemberService;
import club.starcard.modules.member.specification.CrmSpecification;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by wlrllr on 2018/3/14.
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository repository;

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
        if (StringUtils.isNotBlank(member.getBankName()))
            m.setBankName(member.getBankName());
        if (StringUtils.isNotBlank(member.getBankAccount()))
            m.setBankAccount(member.getBankAccount());
        if(StringUtils.isNotBlank(member.getSex()))
            m.setSex(member.getSex());
        if(StringUtils.isNotBlank(member.getNickName())){
            m.setNickName(member.getNickName());
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
}
