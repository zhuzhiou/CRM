package club.starcard.modules.user.service;

import club.starcard.modules.user.entity.SysUser;
import club.starcard.modules.user.repository.SysUserRepository;
import club.starcard.util.SnowflakeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<SysUser> getAllSysUsers(Pageable pageable) {
        return sysUserRepository.findAll(new Specification<SysUser>() {
            @Override
            public Predicate toPredicate(Root<SysUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.notEqual(root.get("username"), "sa");
                CriteriaQuery<?> cq = criteriaQuery.where(predicate);
                return cq.getRestriction();
            }
        }, pageable);
    }

    @Override
    public SysUser getSysUserByUserid(long userId) {
        return sysUserRepository.findOne(userId);
    }

    @Override
    public SysUser getSysUserByUsername(String username) {
        return sysUserRepository.getByUsername(username);
    }

    @Override
    public void saveSysUser(SysUser sysUser) {
        sysUser.setId(SnowflakeGenerator.generator());
        sysUser.setPassword(passwordEncoder.encode("123456"));
        sysUserRepository.save(sysUser);
    }

    @Override
    public void deleteSysUser(Long userid) {
        sysUserRepository.delete(userid);
    }
}
