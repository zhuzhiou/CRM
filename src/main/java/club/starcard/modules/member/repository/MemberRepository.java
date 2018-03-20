package club.starcard.modules.member.repository;

import club.starcard.modules.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Repository
public interface MemberRepository extends JpaRepository<Member,Long> ,JpaSpecificationExecutor<Member> {

    @Query("select m.memberId,m.parentId,m.totalPoint,m.usablePoint,m.name,m.parentName,m.address,m.phone,m.remark,gm.position from Member m,GroupMember gm where gm.groupId=?1 and gm.memberId=m.id")
    List<Object[]> findByGroupId(Long groupId);

    @Modifying
    @Transactional
    @Query("update Member m set m.totalPoint = m.totalPoint+?1,m.usablePoint = m.usablePoint+?1 where m.memberId=?2")
    void addPoint(Long point,Long memberId);

    List<Member> findByParentId(Long parentId);

}
