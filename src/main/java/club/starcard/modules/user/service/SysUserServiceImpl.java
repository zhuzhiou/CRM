package club.starcard.modules.user.service;

import club.starcard.modules.user.entity.SysUser;
import club.starcard.modules.user.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public List<SysUser> getSysUsers() {
        return sysUserRepository.findAll();
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
        sysUserRepository.save(sysUser);
    }
}
