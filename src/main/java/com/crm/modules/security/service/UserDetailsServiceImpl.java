package com.crm.modules.security.service;

import com.crm.modules.userinfo.entity.UserInfo;
import com.crm.modules.userinfo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Assert.notNull(username, "用户名不能为空");
        UserInfo userInfo = userInfoService.getUserInfo(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        User user = new User(username,
                userInfo.getPassword(),
                userInfo.getEnabled(),
                true,
                true,
                true,
                AuthorityUtils.NO_AUTHORITIES);
        return user;
    }
}
