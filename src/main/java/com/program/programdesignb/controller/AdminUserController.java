package com.program.programdesignb.controller;

import com.program.programdesignb.domain.AdminUser;
import com.program.programdesignb.service.AdminUserService;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    public static final String SESSION_ADMIN_ID = "ADMIN_USER_ID";
    public static final String SESSION_ADMIN_NAME = "ADMIN_USERNAME";
    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PostMapping("/login")
    public AdminUser login(@RequestBody LoginRequest request, HttpSession session) {
        AdminUser user = adminUserService.login(request.username, request.password);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        session.setAttribute(SESSION_ADMIN_ID, user.getUserId());
        session.setAttribute(SESSION_ADMIN_NAME, user.getUsername());
        return user;
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(SESSION_ADMIN_ID);
        String username = (String) session.getAttribute(SESSION_ADMIN_NAME);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("username", username);
        result.put("role", "admin");
        return result;
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
