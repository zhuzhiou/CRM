package club.starcard.modules.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wlrllr on 2018/3/20.
 */
@Table(name = "CRM_POINT_LOG")
@Entity
@lombok.Data
public class PointLog  implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Column(name = "OPERATE_TYPE")
    private String operateType;

    @Column(name = "OPERATE_POINT")
    private Long operatePoint;

    @Column(name = "TOTAL_POINT")
    private Long totalPoint;

    @Column(name = "REMARK")
    private String remark;

    @Column(name="CREATE_TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
