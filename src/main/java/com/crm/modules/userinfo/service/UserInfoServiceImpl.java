package com.crm.modules.userinfo.service;

import com.crm.modules.userinfo.entity.UserInfo;
import com.crm.modules.userinfo.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserInfo getUserInfo(String username) {
        return userInfoRepository.getByUsername(username);
    }
}
