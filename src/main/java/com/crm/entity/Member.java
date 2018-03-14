package com.crm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 记录会员的基本信息
 * Created by wlrllr on 2018/3/14.
 */
@Table(name = "CRM_MEMBER")
@Entity
@lombok.Data
public class Member {
    @Id
    @Column(name="ID")
    private Long id;
    /**
     * 邀请人
     */
    @Column(name="INVITER")
    private Long inviter;
    /**
     * 昵称
     */
    @Column(name="NAME")
    private String name;

    @Column(name="ID_CARD")
    private String idCard;

    @Column(name="PHONE")
    private String phone;

    @Column(name="EMAIL")
    private String email;

    /**
     * 入伙金额
     */
    @Column(name="MONEY")
    private Double money;

    /**
     * 受邀时间
     */
    @Column(name="INVITED_TIME")
    private Date invitedTime;
}
