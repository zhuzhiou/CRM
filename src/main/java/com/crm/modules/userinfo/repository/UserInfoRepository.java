package com.crm.modules.userinfo.repository;

import com.crm.modules.userinfo.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfo getByUsername(String username);
}
