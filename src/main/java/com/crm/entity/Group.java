package com.crm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 组信息
 * Created by wlrllr on 2018/3/14.
 */
@Table(name = "CRM_GROUP")
@Entity
@lombok.Data
public class Group {
    @Id
    @Column
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "GROUP_SIZE")
    private Integer groupSize;
    @Column(name = "CURRENT_SIZE")
    private Integer currentSize;
    @Column(name = "CREATE_TIME")
    private Date createTime;
}
