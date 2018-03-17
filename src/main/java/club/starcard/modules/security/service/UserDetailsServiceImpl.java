package club.starcard.modules.security.service;

import club.starcard.modules.user.entity.SysUser;
import club.starcard.modules.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Assert.notNull(username, "用户名不能为空");
        SysUser sysUser = sysUserService.getUserInfo(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return sysUser;
    }
}
