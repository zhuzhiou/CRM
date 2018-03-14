package com.crm.vo;

/**
 * Created by wlrllr on 2018/3/14.
 */
@lombok.Data
public class GroupMemberVo {
    private Long memberId;
    private String memberName;
    private Long groupId;
    private Integer position;
    private Integer groupSize;
    private Integer currentSize;
}
