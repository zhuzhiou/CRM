package club.starcard.modules.member.service;

import club.starcard.modules.member.entity.Group;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface GroupService {
    Page<Group> page(Group group, int pageNum);

    Page<Group> page(Date beginDate, Date endDate, String search, Integer pageNum, Integer pageSize);

    List<Group> findGroupByMemberId(Long parentId);

    List<Group> findLastGroup();
}
