package com.crm.repository;

import com.crm.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {

    @Query("select gm.memberId,g.groupId,gm.position,g.groupSize,g.currentSize from Group g,GroupMember gm where g.id=gm.groupId and g.status='Y' and gm.memberId=:memberId")
    List<Group> findGroupByMemberId(Long memberId);

    @Query("update Group g set g.currentSize=?1 where g.groupId=?2")
    void updateCurrentSize(Integer size,Long groupId);

    /**
     * 组满员
     */
    @Query("update Group g set g.status='N',g.endTime=?1 where g.groupId=?2")
    void filledGroup(Date endTime, Long groupId);

    @Query("select gm.memberId,g.groupId,gm.position,g.groupSize,g.currentSize from Group g,GroupMember gm where g.id=gm.groupId and g.status='Y' order by g.groupId asc ")
    List<Group> findLastGroup();
}
