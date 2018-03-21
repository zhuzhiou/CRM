package club.starcard.modules.user.service;

import club.starcard.modules.user.entity.SysUser;
import club.starcard.modules.user.repository.SysUserRepository;
import club.starcard.util.SnowflakeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<SysUser> getAllSysUsers(Pageable pageable) {
        return sysUserRepository.findAll(pageable);
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
}
