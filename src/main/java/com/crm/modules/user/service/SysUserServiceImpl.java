package com.crm.modules.user.service;

import com.crm.modules.user.entity.SysUser;
import com.crm.modules.user.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public SysUser getUserInfo(String username) {
        return sysUserRepository.getByUsername(username);
    }
}
