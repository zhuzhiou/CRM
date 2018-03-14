package com.crm.repository;

import com.crm.entity.Group;
import com.crm.vo.GroupMemberVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {

    @Query("select gm.memberId,gm.groupId,gm.position,g.groupSize,g.currentSize from Group g,GroupMember gm where g.id=gm.groupId and g.status='Y' and gm.memberId=:memberId")
    List<GroupMemberVo> findGroupByMemberId(Long memberId);
}
