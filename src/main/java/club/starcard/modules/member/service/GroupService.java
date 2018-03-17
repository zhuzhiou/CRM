package club.starcard.modules.member.service;

import club.starcard.modules.member.entity.Group;
import org.springframework.data.domain.Page;

public interface GroupService {
    Page<Group> page(Group group, int pageNum);
}
