package club.starcard.modules.user.service;

import club.starcard.modules.user.entity.SysUser;

import java.util.List;

public interface SysUserService {

    List<SysUser> getSysUsers();

    SysUser getSysUserByUserid(long userId);

    SysUser getSysUserByUsername(String username);

    void saveSysUser(SysUser sysUser);
}
