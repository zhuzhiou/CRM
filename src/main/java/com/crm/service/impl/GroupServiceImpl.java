package com.crm.service.impl;

import com.crm.constant.CommonConstant;
import com.crm.entity.Group;
import com.crm.repository.GroupRepository;
import com.crm.service.GroupService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by wlrllr on 2018/3/14.
 */
@Service
public class GroupServiceImpl implements GroupService {

    private GroupRepository repository;

    @Override
    public Page<Group> page(Group group,int pageNum){
        Pageable pageable = new PageRequest(pageNum, CommonConstant.pageSize);
        if(group == null){
            return repository.findAll(pageable);
        }
        return  repository.findAll(Example.of(group),pageable);
    }
}
