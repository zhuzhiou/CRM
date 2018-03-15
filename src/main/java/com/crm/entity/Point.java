package com.crm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by wlrllr on 2018/3/14.
 */
@Table(name = "MMC_POINT")
@Entity
@lombok.Data
public class Point  implements Serializable {
    @Id
    @Column(name="POINT_ID")
    private Long pointId;
    @Column(name="MEMBER_ID")
    private Long memberId;
    @Column(name="POINT")
    private Long point;
}
