package club.starcard.modules.user.service;

import club.starcard.modules.user.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SysUserService {

    Page<SysUser> getAllSysUsers(Pageable pageable);

    SysUser getSysUserByUserid(long userId);

    SysUser getSysUserByUsername(String username);

    void saveSysUser(SysUser sysUser);
}
