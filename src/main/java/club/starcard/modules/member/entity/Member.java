package club.starcard.modules.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 记录会员的基本信息
 * Created by wlrllr on 2018/3/14.
 */
@Table(name = "CRM_MEMBER")
@Entity
@lombok.Data
public class Member implements Serializable {


    @Id
    @Column(name = "MEMBER_ID")
    private Long memberId;

    /**
     * 邀请人
     */
    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "PARENT_NAME")
    private String parentName;
    /**
     * 昵称
     */
    @Column(name = "NICK_NAME")
    private String nickName;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SEX")
    private String sex;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ID_CARD")
    private String idCard;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name="BANK_ACCOUNT")
    private String bankAccount;

    @Column(name="BANK_NAME")
    private String bankName;

    /**
     * 入伙金额
     */
    @Column(name = "MONEY")
    private Double money;

    /**
     * 入會时间
     */
    @Column(name = "CREATE_TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private Date createTime;

    @Column(name = "REMARK")
    private String remark;
}
