package com.program.programdesignb.controller;

import com.program.programdesignb.domain.AdminUser;
import com.program.programdesignb.service.AdminUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PostMapping("/login")
    public AdminUser login(@RequestBody LoginRequest request) {
        AdminUser user = adminUserService.login(request.username, request.password);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        return user;
    }

    @PostMapping("/create")
    public AdminUser create(@RequestBody AdminUser adminUser) {
        AdminUser created = adminUserService.createAdminUser(adminUser);
        if (created == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Create admin failed");
        }
        return created;
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }
}
