package com.crm.service;

import com.crm.entity.Member;
import com.crm.vo.MemberVo;

import java.util.List;

/**
 * Created by wlrllr on 2018/3/14.
 */
public interface GroupService {

    boolean qualifying(Member member);

    boolean initData(List<MemberVo> vos);
}
