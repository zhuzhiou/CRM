package club.starcard.modules.member.service.impl;

import club.starcard.modules.member.constant.CommonConstant;
import club.starcard.modules.member.entity.Group;
import club.starcard.modules.member.repository.GroupRepository;
import club.starcard.modules.member.service.GroupService;
import club.starcard.modules.member.specification.CrmSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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
        //return repository.findAll(pageable);
        Page<Object[]> page = repository.page("%"+search+"%",pageable);
        List<Group> list = covertPageData(page);
        return new PageImpl(list,pageable,page.getTotalElements());
        //return repository.findAll(pageable);
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

    private List<Group> covertPageData(Page<Object[]> data){
        List<Group> list = new ArrayList<>();
        List<Object[]> content = data.getContent();
        if(data != null && content.size()>0){
            for(Object[] obj : data){
                Group g = new Group();
                BigInteger groupId = (BigInteger)obj[0];
                g.setGroupId(groupId.longValue());
                g.setName((String)obj[1]);
                g.setCurrentSize((Integer)obj[2]);
                g.setStatus((String)obj[3]);
                g.setMember((String)obj[4]);
                g.setCreateTime((Date)obj[5]);
                list.add(g);
            }
        }
        return list;
    }

    private List<Group> covertData(List<Object[]> data){
        List<Group> list = new ArrayList<>();
        if(data != null && data.size()>0){
            for(Object[] obj : data){
                Group g = new Group();
                g.setMemberId(((BigInteger)obj[0]).longValue());
                g.setGroupId(((BigInteger)obj[1]).longValue());
                g.setPosition((Integer)obj[2]);
                g.setGroupSize((Integer)obj[3]);
                g.setCurrentSize((Integer)obj[4]);
                list.add(g);
            }
        }
        return list;
    }
}
