
package club.starcard.modules.member.service;

import club.starcard.modules.member.entity.Member;
import org.springframework.data.domain.Page;

import java.util.Date;

/**
 * Created by wlrllr on 2018/3/14.
 */
public interface MemberService {

    Page<Member> page(Date beginDate, Date endDate, String search, Integer pageNum, Integer pageSize);

    boolean updateSelection(Member member);

    Member get(Long memberId);
}
