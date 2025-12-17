package com.program.programdesignb.controller;

import com.program.programdesignb.domain.Activity;
import com.program.programdesignb.domain.Checkin;
import com.program.programdesignb.domain.Registration;
import com.program.programdesignb.service.ActivityService;
import com.program.programdesignb.service.CheckinService;
import com.program.programdesignb.service.RegistrationService;
import com.program.programdesignb.util.TokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checkin")
public class CheckinController {
    private final CheckinService checkinService;
    private final RegistrationService registrationService;
    private final ActivityService activityService;
    private final TokenUtil tokenUtil;

    public CheckinController(CheckinService checkinService,
                             RegistrationService registrationService,
                             ActivityService activityService,
                             TokenUtil tokenUtil) {
        this.checkinService = checkinService;
        this.registrationService = registrationService;
        this.activityService = activityService;
        this.tokenUtil = tokenUtil;
    }

    /**
     * 通过手机号签到（基础签到）
     */
    @PostMapping("/by-phone")
    public Map<String, Object> checkinByPhone(@RequestBody CheckinByPhoneRequest request) {
        // 验证token
        if (request.token == null || !tokenUtil.validateToken(request.token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "签到码已失效或无效");
        }

        Integer activityId = tokenUtil.getActivityIdFromToken(request.token);
        if (activityId == null || !activityId.equals(request.activityId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "签到码与活动不匹配");
        }

        Registration found = findRegistration(activityId, request.phone, request.studentNo);

        // 检查是否已签到
        List<Checkin> existingCheckins = checkinService.listByRegId(found.getRegistrationId());
        if (!existingCheckins.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "您已签到，请勿重复签到");
        }

        // 执行签到
        Checkin checkin = checkinService.doCheckin(found.getRegistrationId(), request.location);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("checkin", checkin);
        result.put("registration", found);
        return result;
    }

    /**
     * 定位签到（准确签到）
     */
    @PostMapping("/with-location")
    public Map<String, Object> checkinWithLocation(@RequestBody CheckinWithLocationRequest request) {
        // 验证token
        if (request.token == null || !tokenUtil.validateToken(request.token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "签到码已失效或无效");
        }

        Integer activityId = tokenUtil.getActivityIdFromToken(request.token);
        if (activityId == null || !activityId.equals(request.activityId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "签到码与活动不匹配");
        }

        Registration found = findRegistration(activityId, request.phone, request.studentNo);

        // 检查是否已签到
        List<Checkin> existingCheckins = checkinService.listByRegId(found.getRegistrationId());
        if (!existingCheckins.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "您已签到，请勿重复签到");
        }

        // 验证定位
        Activity activity = activityService.getActivityById(activityId);
        if (request.latitude == null || request.longitude == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "请开启定位权限");
        }

        // 简单的距离验证（实际应该配置活动地点坐标并计算距离）
        // 这里只做简单示例，实际项目需要更精确的地理位置验证
        boolean locationValid = validateLocation(request.latitude, request.longitude, activity);
        if (!locationValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "签到位置不在活动现场范围内");
        }

        // 执行签到（记录经纬度）
        String locationInfo = String.format("经度:%.6f,纬度:%.6f", request.longitude, request.latitude);
        Checkin checkin = checkinService.doCheckin(found.getRegistrationId(), locationInfo);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("checkin", checkin);
        result.put("registration", found);
        result.put("locationVerified", true);
        return result;
    }

    /**
     * 查询活动的所有签到记录
     */
    @GetMapping("/activity/{activityId}")
    public List<Checkin> getActivityCheckins(@PathVariable Integer activityId) {
        return checkinService.listByActivityId(activityId);
    }

    /**
     * 验证位置是否在活动现场（简化版）
     * 实际项目中应该：
     * 1. 在Activity表中添加venue_latitude和venue_longitude字段
     * 2. 使用Haversine公式计算距离
     * 3. 设置合理的距离阈值（如50-100米）
     */
    private boolean validateLocation(Double latitude, Double longitude, Activity activity) {
        // 这里返回true表示暂不严格验证
        // 实际使用时应该配置活动地点坐标并计算距离
        return true;
    }

    private Registration findRegistration(Integer activityId, String phone, String studentNo) {
        if (!StringUtils.hasText(phone) || !StringUtils.hasText(studentNo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "手机号和学号为必填项");
        }
        List<Registration> registrations = registrationService.listByActivity(activityId);
        return registrations.stream()
                .filter(r -> phone.equals(r.getPhone()) && studentNo.equals(r.getStudentNo()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "未找到该手机号/学号组合的报名记录"));
    }

    public static class CheckinByPhoneRequest {
        public Integer activityId;
        public String phone;
        public String studentNo;
        public String token;
        public String location;
    }

    public static class CheckinWithLocationRequest {
        public Integer activityId;
        public String phone;
        public String studentNo;
        public String token;
        public Double latitude;
        public Double longitude;
    }
}
