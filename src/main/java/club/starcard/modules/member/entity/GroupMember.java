package club.starcard.modules.member.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by wlrllr on 2018/3/14.
 */
@Table(name = "CRM_GROUP_MEMBER")
@Entity
@lombok.Data
@IdClass(GroupMemberMultiKeys.class)
public class GroupMember  implements Serializable {

    @Id
    @Column(name="MEMBER_ID")
    private Long memberId;
    @Id
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
