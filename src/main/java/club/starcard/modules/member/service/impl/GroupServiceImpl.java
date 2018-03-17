package club.starcard.modules.member.service.impl;

import club.starcard.modules.member.constant.CommonConstant;
import club.starcard.modules.member.entity.Group;
import club.starcard.modules.member.repository.GroupRepository;
import club.starcard.modules.member.service.GroupService;
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
