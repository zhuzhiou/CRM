package com.crm.service.impl;

import com.crm.constant.CommonConstant;
import com.crm.entity.Member;
import com.crm.repository.MemberRepository;
import com.crm.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by wlrllr on 2018/3/14.
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository repository;

    @Override
    public Page<Member> page(Member member, Integer pageNum, Integer pageSize){
        if(pageSize == null){
            pageSize = CommonConstant.pageSize;
        }
        //pageable从0页开始
        Pageable pageable = new PageRequest(pageNum-1,pageSize);
        if(member == null){
            return repository.findAll(pageable);
        }
        return  repository.findAll(Example.of(member),pageable);
    }
}
