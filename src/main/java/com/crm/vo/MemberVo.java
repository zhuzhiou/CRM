package com.crm.vo;

import com.crm.entity.Member;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by wlrllr on 2018/3/14.
 */
@lombok.Data
public class MemberVo {
    /**
     * 邀请人
     */
    private Long parentId;
    /**
     * 昵称
     */
    private String name;

    private String idCard;

    private String phone;

    private String email;

    /**
     * 入伙金额
     */
    private Double money;

    /**
     * 受邀时间
     */
    private Date invitedTime;

    public Member getMember(){
        Member member = new Member();
        member.setParentId(this.parentId);
        member.setName(this.name);
        member.setIdCard(this.idCard);
        member.setPhone(this.phone);
        member.setEmail(this.email);
        return member;
    }
}
