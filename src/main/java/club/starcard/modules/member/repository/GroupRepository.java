package club.starcard.modules.member.repository;

import club.starcard.modules.member.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {

    @Query("select gm.memberId,g.groupId,gm.position,g.groupSize,g.currentSize from Group g,GroupMember gm where g.id=gm.groupId and g.status='Y' and gm.memberId=?1")
    List<Object[]> findGroupByMemberId(Long memberId);

    @Modifying
    @Transactional
    @Query("update Group g set g.currentSize=?1 where g.groupId=?2")
    void updateCurrentSize(Integer size,Long groupId);

    /**
     * 组满员
     */
    @Modifying
    @Transactional
    @Query("update Group g set g.status='N',g.endTime=?1 where g.groupId=?2")
    void filledGroup(Date endTime, Long groupId);

    @Query("select gm.memberId,g.groupId,gm.position,g.groupSize,g.currentSize from Group g,GroupMember gm where g.id=gm.groupId and g.status='Y' order by g.groupId asc ")
    List<Object[]> findLastGroup();

    @Query(nativeQuery=true,value="SELECT g.group_id,g.name,g.current_size,g.status, GROUP_CONCAT(m.name) as member,g.create_time FROM crm_group g, crm_group_member gm, crm_member m WHERE g.group_id = gm.group_id AND gm.member_id = m.member_id AND m.name LIKE ?1 GROUP BY group_id \n#pageable\n"
            ,countQuery = "SELECT count(1) FROM ( SELECT g.group_id, g. NAME, g.current_size, g.status, GROUP_CONCAT(m.name) AS member, g.create_time FROM crm_group g, crm_group_member gm, crm_member m WHERE g.group_id = gm.group_id AND gm.member_id = m.member_id AND m.name LIKE ?1 GROUP BY group_id ) t")
    Page<Object[]> page(String name, Pageable pageable);
}
