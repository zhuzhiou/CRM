package com.crm.service;

import com.crm.entity.Member;
import org.springframework.data.domain.Page;

/**
 * Created by wlrllr on 2018/3/14.
 */
public interface MemberService {

    Page<Member> page(Member member, Integer pageNum, Integer pageSize);
}
