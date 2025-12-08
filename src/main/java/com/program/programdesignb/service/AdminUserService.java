package com.program.programdesignb.service;

import com.program.programdesignb.domain.AdminUser;

public interface AdminUserService {
    AdminUser login(String username, String rawPassword);

    AdminUser createAdminUser(AdminUser adminUser);

    AdminUser findByUsername(String username);
}
