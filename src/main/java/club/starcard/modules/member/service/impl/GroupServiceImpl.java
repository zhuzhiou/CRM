package club.starcard.modules.member.service.impl;

import club.starcard.modules.member.constant.CommonConstant;
import club.starcard.modules.member.entity.Group;
import club.starcard.modules.member.repository.GroupRepository;
import club.starcard.modules.member.service.GroupService;
import club.starcard.modules.member.specification.CrmSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wlrllr on 2018/3/14.
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository repository;

    @Override
    public Page<Group> page(Group group,int pageNum){
        Pageable pageable = new PageRequest(pageNum, CommonConstant.pageSize);
        if(group == null){
            return repository.findAll(pageable);
        }
        return  repository.findAll(Example.of(group),pageable);
    }

    @Override
    public Page<Group> page(Date beginDate, Date endDate, String search, Integer pageNum, Integer pageSize) {
        if (pageSize == null) {
            pageSize = CommonConstant.pageSize;
        }
        //pageable从0页开始
        Pageable pageable = new PageRequest(pageNum - 1, pageSize);
        //FIXME 这个sql还没搞定啊
        return repository.findAll(pageable);
        //return null;//repository.findAll(CrmSpecification.queryMemberSpecification(beginDate, endDate, search), pageable);
    }

    @Override
    public List<Group> findGroupByMemberId(Long parentId) {
        return covertData(repository.findGroupByMemberId(parentId));
    }

    @Override
    public List<Group> findLastGroup() {
        return covertData(repository.findLastGroup());
    }

    private List<Group> covertData(List<Object[]> data){
        List<Group> list = new ArrayList<>();
        if(data != null && data.size()>0){
            for(Object[] obj : data){
                Group g = new Group();
                g.setMemberId((Long)obj[0]);
                g.setGroupId((Long)obj[1]);
                g.setPosition((Integer)obj[2]);
                g.setGroupSize((Integer)obj[3]);
                g.setCurrentSize((Integer)obj[4]);
                list.add(g);
            }
        }
        return list;
    }
}
