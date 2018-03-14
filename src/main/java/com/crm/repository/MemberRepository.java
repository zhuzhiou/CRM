package com.crm.repository;

import com.crm.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    @Query("select m.id,m.inviter from Member m,GroupMember gm where gm.groupId=:groupId and gm.position=1 and gm.memberId=m.id")
    Member find1ByGroupId(Long groupId);

}
