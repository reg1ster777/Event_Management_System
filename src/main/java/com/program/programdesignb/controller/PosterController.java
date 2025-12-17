package com.program.programdesignb.controller;

import com.program.programdesignb.domain.Activity;
import com.program.programdesignb.service.ActivityService;
import com.program.programdesignb.util.QRCodeUtil;
import com.program.programdesignb.util.TokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/poster")
public class PosterController {

    private final ActivityService activityService;
    private final QRCodeUtil qrCodeUtil;
    private final TokenUtil tokenUtil;

    public PosterController(ActivityService activityService, QRCodeUtil qrCodeUtil, TokenUtil tokenUtil) {
        this.activityService = activityService;
        this.qrCodeUtil = qrCodeUtil;
        this.tokenUtil = tokenUtil;
    }

    /**
     * 生成报名二维码图片
     */
    @GetMapping(value = "/qrcode/registration/{activityId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateRegistrationQRCode(@PathVariable Integer activityId,
                                                             @RequestParam(required = false) String baseUrl) {
        Activity activity = activityService.getActivityById(activityId);
        if (activity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "活动不存在");
        }

        // 生成带token的报名链接
        Date signupEndTime = Date.from(activity.getSignupEndTime().atZone(ZoneId.systemDefault()).toInstant());
        String token = tokenUtil.generateRegistrationToken(activityId, signupEndTime);

        // 构建报名URL
        String base = (baseUrl != null && !baseUrl.isEmpty()) ? baseUrl : "http://localhost:8088";
        String url = base + "/register.html?activityId=" + activityId + "&token=" + token;

        try {
            byte[] qrCodeImage = qrCodeUtil.generateQRCode(url, 400, 400);
            return ResponseEntity.ok()
                    .contentType(Objects.requireNonNull(MediaType.IMAGE_PNG))
                    .body(qrCodeImage);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "生成二维码失败: " + e.getMessage());
        }
    }

    /**
     * 生成签到二维码图片
     */
    @GetMapping(value = "/qrcode/checkin/{activityId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateCheckinQRCode(@PathVariable Integer activityId,
                                                        @RequestParam(required = false) String baseUrl) {
        Activity activity = activityService.getActivityById(activityId);
        if (activity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "活动不存在");
        }

        // 生成带token的签到链接
        Date startTime = Date.from(activity.getStartTime().atZone(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(activity.getEndTime().atZone(ZoneId.systemDefault()).toInstant());
        String token = tokenUtil.generateCheckinToken(activityId, startTime, endTime);

        // 构建签到URL
        String base = (baseUrl != null && !baseUrl.isEmpty()) ? baseUrl : "http://localhost:8088";
        String url = base + "/checkin.html?activityId=" + activityId + "&token=" + token;

        try {
            byte[] qrCodeImage = qrCodeUtil.generateQRCode(url, 400, 400);
            return ResponseEntity.ok()
                    .contentType(Objects.requireNonNull(MediaType.IMAGE_PNG))
                    .body(qrCodeImage);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "生成二维码失败: " + e.getMessage());
        }
    }

    /**
     * 验证token是否有效
     */
    @GetMapping("/validate-token")
    public Map<String, Object> validateToken(@RequestParam String token) {
        Map<String, Object> result = new HashMap<>();
        boolean valid = tokenUtil.validateToken(token);
        result.put("valid", valid);

        if (valid) {
            Integer activityId = tokenUtil.getActivityIdFromToken(token);
            result.put("activityId", activityId);
        }

        return result;
    }
}
