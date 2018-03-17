package com.crm.service;

import com.crm.entity.Group;
import org.springframework.data.domain.Page;

public interface GroupService {
    Page<Group> page(Group group, int pageNum);
}
