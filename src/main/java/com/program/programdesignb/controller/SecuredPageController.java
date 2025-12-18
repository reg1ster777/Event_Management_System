package com.program.programdesignb.controller;

import com.program.programdesignb.domain.Activity;
import com.program.programdesignb.service.ActivityService;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Controller
public class SecuredPageController {

    private final ActivityService activityService;

    public SecuredPageController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping(value = "/checkin-qrcode.html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<Resource> checkinPage(@RequestParam Integer activityId, HttpSession session) throws IOException {
        ResponseEntity<Resource> guard = guardActivityAccess(activityId, session,
                "未找到该活动，无法生成签到二维码",
                "仅活动创建者可以查看签到二维码");
        if (guard != null) {
            return guard;
        }
        ClassPathResource html = new ClassPathResource("static/checkin-qrcode.html");
        return ResponseEntity.ok()
                .contentType(Objects.requireNonNull(MediaType.TEXT_HTML))
                .body(html);
    }

    @GetMapping(value = "/statistics.html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<Resource> statisticsPage(@RequestParam Integer activityId, HttpSession session) throws IOException {
        ResponseEntity<Resource> guard = guardActivityAccess(activityId, session,
                "未找到该活动，无法查看统计信息",
                "仅活动创建者可以查看统计页面");
        if (guard != null) {
            return guard;
        }
        ClassPathResource html = new ClassPathResource("static/statistics.html");
        return ResponseEntity.ok()
                .contentType(Objects.requireNonNull(MediaType.TEXT_HTML))
                .body(html);
    }

    private ResponseEntity<Resource> guardActivityAccess(Integer activityId,
                                                         HttpSession session,
                                                         String notFoundMessage,
                                                         String forbidMessage) {
        Integer adminId = (Integer) session.getAttribute(AdminUserController.SESSION_ADMIN_ID);
        if (adminId == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(java.net.URI.create("/login.html"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        Activity activity = activityService.getActivityById(activityId);
        if (activity == null) {
            return forbiddenResponse(notFoundMessage);
        }
        Integer ownerId = activity.getCreatedBy();
        if (ownerId == null || !ownerId.equals(adminId)) {
            return forbiddenResponse(forbidMessage);
        }
        return null;
    }

    private ResponseEntity<Resource> forbiddenResponse(String message) {
        String body = """
                <!DOCTYPE html>
                <html lang="zh-CN">
                <head><meta charset="UTF-8"><title>无权访问</title></head>
                <body style="font-family:Arial;padding:40px;">
                    <h2>无权访问</h2>
                    <p>%s</p>
                    <a href="/admin-activities.html">返回活动列表</a>
                </body>
                </html>
                """.formatted(message);
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        Resource resource = new ByteArrayResource(Objects.requireNonNull(bytes));
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(Objects.requireNonNull(MediaType.TEXT_HTML))
                .body(resource);
    }
}
