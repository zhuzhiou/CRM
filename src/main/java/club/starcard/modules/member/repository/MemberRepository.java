package club.starcard.modules.member.repository;

import club.starcard.modules.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface MemberRepository extends JpaRepository<Member,Long> ,JpaSpecificationExecutor<Member> {

    @Query("select m.memberId,m.parentId from Member m,GroupMember gm where gm.groupId=:groupId and gm.position=1 and gm.memberId=m.id")
    Member find1ByGroupId(Long groupId);

}
