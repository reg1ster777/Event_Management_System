package com.program.programdesignb.service.impl;

import com.program.programdesignb.domain.AdminUser;
import com.program.programdesignb.mapper.AdminUserMapper;
import com.program.programdesignb.service.AdminUserService;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    private final AdminUserMapper adminUserMapper;

    public AdminUserServiceImpl(AdminUserMapper adminUserMapper) {
        this.adminUserMapper = adminUserMapper;
    }

    @Override
    public AdminUser login(String username, String rawPassword) {
        AdminUser user = adminUserMapper.findByUsername(username);
        if (user == null) {
            return null;
        }
        return rawPassword != null && rawPassword.equals(user.getPassword()) ? user : null;
    }

    @Override
    public AdminUser createAdminUser(AdminUser adminUser) {
        adminUserMapper.insert(adminUser);
        return adminUser;
    }

    @Override
    public AdminUser findByUsername(String username) {
        return adminUserMapper.findByUsername(username);
    }
}
