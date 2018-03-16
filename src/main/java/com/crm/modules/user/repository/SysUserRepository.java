package com.crm.modules.user.repository;

import com.crm.modules.user.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    SysUser getByUsername(String username);
}
