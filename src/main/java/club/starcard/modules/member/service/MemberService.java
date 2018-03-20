
package club.starcard.modules.member.service;

import club.starcard.modules.member.entity.Member;
import club.starcard.modules.member.entity.PointLog;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * Created by wlrllr on 2018/3/14.
 */
public interface MemberService {

    Page<Member> page(Date beginDate, Date endDate, String search, Integer pageNum, Integer pageSize);

    boolean updateSelection(Member member);

    Member get(Long memberId);

    void rewardMember(Long memberId,Long totalPoint,Long rewardPoint,String remark);

    Member find1ByGroupId( Long groupId);

    List<Member> findByGroupId( Long groupId);

    Page<PointLog> pagePointLog(Long memberId, Integer pageNum, Integer pageSize);

    List<Member> findByParentId(Long parentId);

    List<PointLog> findPointLogs(Long memberId);



}
