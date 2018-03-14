package com.crm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wlrllr on 2018/3/14.
 */
@Table(name = "CRM_GROUP_MEMBER")
@Entity
@lombok.Data
public class GroupMember {

    @Column(name="MEMBER_ID")
    private Long memberId;
    @Column(name="GROUP_ID")
    private Long groupId;
    @Column(name="POSITION")
    private int position;

    public GroupMember() {
    }

    public GroupMember(Long memberId, Long groupId, int position) {
        this.memberId = memberId;
        this.groupId = groupId;
        this.position = position;
    }
}
