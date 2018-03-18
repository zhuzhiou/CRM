package club.starcard.modules.member.service;

import club.starcard.modules.member.entity.Member;
import club.starcard.modules.member.vo.MemberVo;

/**
 * Created by wlrllr on 2018/3/14.
 */
public interface InviteService {

    boolean qualifying(Member member);

    boolean crmOpen(Member member);
}
