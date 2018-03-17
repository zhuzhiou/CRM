package club.starcard.modules.member.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 组信息
 * Created by wlrllr on 2018/3/14.
 */
@Table(name = "CRM_GROUP")
@Entity
@lombok.Data
public class Group  implements Serializable {
    @Id
    @Column(name="GROUP_ID")
    private Long groupId;
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
    @Column(name = "END_TIME")
    private Date endTime;
    @Transient
    private Long memberId;
    @Transient
    private Integer position;
}
