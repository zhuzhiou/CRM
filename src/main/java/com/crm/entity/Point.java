package com.mmc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by wlrllr on 2018/3/14.
 */
@Table(name = "MMC_POINT")
@Entity
@lombok.Data
public class Point {
    @Id
    @Column(name="ID")
    private Long id;
    @Column(name="MEMBER_ID")
    private Long memberId;
    @Column(name="POINT")
    private Long point;
}
