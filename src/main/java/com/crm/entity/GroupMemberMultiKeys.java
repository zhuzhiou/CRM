package com.crm.entity;

import java.io.Serializable;

/**
 * Created by wlrllr on 2018/3/16.
 */
public class GroupMemberMultiKeys implements Serializable {
    private Long memberId;
    private Long groupId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupMemberMultiKeys that = (GroupMemberMultiKeys) o;

        if (!memberId.equals(that.memberId)) return false;
        return groupId.equals(that.groupId);
    }

    @Override
    public int hashCode() {
        int result = memberId.hashCode();
        result = 31 * result + groupId.hashCode();
        return result;
    }
}
