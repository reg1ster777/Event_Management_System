package com.program.programdesignb.controller;

import com.program.programdesignb.domain.Activity;
import com.program.programdesignb.domain.Checkin;
import com.program.programdesignb.domain.Registration;
import com.program.programdesignb.service.ActivityService;
import com.program.programdesignb.service.CheckinService;
import com.program.programdesignb.service.RegistrationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final ActivityService activityService;
    private final RegistrationService registrationService;
    private final CheckinService checkinService;

    public StatisticsController(ActivityService activityService,
                                RegistrationService registrationService,
                                CheckinService checkinService) {
        this.activityService = activityService;
        this.registrationService = registrationService;
        this.checkinService = checkinService;
    }

    /**
     * 获取活动统计数据
     */
    @GetMapping("/activity/{activityId}")
    public Map<String, Object> getActivityStatistics(@PathVariable Integer activityId) {
        Activity activity = activityService.getActivityById(activityId);
        if (activity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "活动不存在");
        }

        List<Registration> registrations = registrationService.listByActivity(activityId);

        // 统计签到数
        int checkedInCount = 0;
        for (Registration reg : registrations) {
            List<Checkin> checkins = checkinService.listByRegId(reg.getRegistrationId());
            if (!checkins.isEmpty()) {
                checkedInCount++;
            }
        }

        // 按学院统计
        Map<String, Long> collegeStats = registrations.stream()
                .filter(r -> r.getCollege() != null && !r.getCollege().isEmpty())
                .collect(Collectors.groupingBy(Registration::getCollege, Collectors.counting()));

        // 按班级统计
        Map<String, Long> classStats = registrations.stream()
                .filter(r -> r.getClazz() != null && !r.getClazz().isEmpty())
                .collect(Collectors.groupingBy(Registration::getClazz, Collectors.counting()));

        Map<String, Object> result = new HashMap<>();
        result.put("activity", activity);
        result.put("totalRegistrations", registrations.size());
        result.put("checkedInCount", checkedInCount);
        result.put("checkInRate", registrations.isEmpty() ? 0 : (checkedInCount * 100.0 / registrations.size()));
        result.put("maxParticipants", activity.getMaxParticipants());
        result.put("remainingSlots", activity.getMaxParticipants() != null ?
                Math.max(0, activity.getMaxParticipants() - registrations.size()) : null);
        result.put("collegeDistribution", collegeStats);
        result.put("classDistribution", classStats);

        return result;
    }

    /**
     * 导出报名名单为CSV
     */
    @GetMapping("/activity/{activityId}/export")
    public ResponseEntity<byte[]> exportRegistrations(@PathVariable Integer activityId) {
        Activity activity = activityService.getActivityById(activityId);
        if (activity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "活动不存在");
        }

        List<Registration> registrations = registrationService.listByActivity(activityId);

        // 构建CSV内容
        StringBuilder csv = new StringBuilder();
        csv.append("\uFEFF"); // UTF-8 BOM
        csv.append("报名ID,姓名,手机号,学号,学校,学院,班级,邮箱,报名时间,签到状态\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Registration reg : registrations) {
            List<Checkin> checkins = checkinService.listByRegId(reg.getRegistrationId());
            String checkinStatus = checkins.isEmpty() ? "未签到" : "已签到";

            csv.append(reg.getRegistrationId()).append(",")
                    .append(escapeCSV(reg.getName())).append(",")
                    .append(escapeCSV(reg.getPhone())).append(",")
                    .append(escapeCSV(reg.getStudentNo())).append(",")
                    .append(escapeCSV(reg.getSchool())).append(",")
                    .append(escapeCSV(reg.getCollege())).append(",")
                    .append(escapeCSV(reg.getClazz())).append(",")
                    .append(escapeCSV(reg.getEmail())).append(",")
                    .append(reg.getCreatedTime() != null ? reg.getCreatedTime().format(formatter) : "").append(",")
                    .append(checkinStatus).append("\n");
        }

        byte[] bytes = csv.toString().getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        String filename = "activity_" + activityId + "_registrations.csv";
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }

    /**
     * 导出签到名单为CSV
     */
    @GetMapping("/activity/{activityId}/export-checkins")
    public ResponseEntity<byte[]> exportCheckins(@PathVariable Integer activityId) {
        Activity activity = activityService.getActivityById(activityId);
        if (activity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "活动不存在");
        }

        List<Registration> registrations = registrationService.listByActivity(activityId);

        // 构建CSV内容
        StringBuilder csv = new StringBuilder();
        csv.append("\uFEFF"); // UTF-8 BOM
        csv.append("签到ID,姓名,手机号,学号,学院,班级,签到时间,签到地点\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Registration reg : registrations) {
            List<Checkin> checkins = checkinService.listByRegId(reg.getRegistrationId());

            if (!checkins.isEmpty()) {
                Checkin checkin = checkins.get(0); // 取第一条签到记录
                csv.append(checkin.getCheckinId()).append(",")
                        .append(escapeCSV(reg.getName())).append(",")
                        .append(escapeCSV(reg.getPhone())).append(",")
                        .append(escapeCSV(reg.getStudentNo())).append(",")
                        .append(escapeCSV(reg.getCollege())).append(",")
                        .append(escapeCSV(reg.getClazz())).append(",")
                        .append(checkin.getCheckinTime() != null ? checkin.getCheckinTime().format(formatter) : "").append(",")
                        .append(escapeCSV(checkin.getLocation())).append("\n");
            }
        }

        byte[] bytes = csv.toString().getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        String filename = "activity_" + activityId + "_checkins.csv";
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }

    /**
     * CSV转义处理
     */
    private String escapeCSV(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}