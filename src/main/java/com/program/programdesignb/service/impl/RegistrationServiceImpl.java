package com.program.programdesignb.service.impl;

import com.program.programdesignb.domain.Activity;
import com.program.programdesignb.domain.Registration;
import com.program.programdesignb.mapper.ActivityMapper;
import com.program.programdesignb.mapper.RegistrationMapper;
import com.program.programdesignb.service.RegistrationService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationMapper registrationMapper;
    private final ActivityMapper activityMapper;

    public RegistrationServiceImpl(RegistrationMapper registrationMapper, ActivityMapper activityMapper) {
        this.registrationMapper = registrationMapper;
        this.activityMapper = activityMapper;
    }

    @Override
    public Registration register(Integer activityId, Registration registration) {
        Activity activity = activityMapper.findById(activityId);
        if (activity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "活动不存在");
        }

        // 状态与截止校验
        if (!"报名中".equals(activity.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "当前活动不在报名中，无法报名");
        }
        LocalDateTime now = LocalDateTime.now();
        if (activity.getSignupEndTime() != null && now.isAfter(activity.getSignupEndTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "已过报名截止时间");
        }

        // 人数上限校验
        if (activity.getMaxParticipants() != null) {
            int currentCount = registrationMapper.countByActivityId(activityId);
            if (currentCount >= activity.getMaxParticipants()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "报名人数已满");
            }
        }

        // 重复报名校验
        if (StringUtils.hasText(registration.getPhone())) {
            int duplicateByPhone = registrationMapper.countByActivityIdAndPhone(activityId, registration.getPhone());
            if (duplicateByPhone > 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "该手机号已报名，无需重复提交");
            }
        }
        if (StringUtils.hasText(registration.getStudentNo())) {
            int duplicateByStudentNo = registrationMapper.countByActivityIdAndStudentNo(activityId, registration.getStudentNo());
            if (duplicateByStudentNo > 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "该学号已报名，无需重复提交");
            }
        }

        registration.setActivityId(activityId);
        registration.setCreatedTime(now);
        registrationMapper.insert(registration);
        return registration;
    }

    @Override
    public List<Registration> listByActivity(Integer activityId) {
        return registrationMapper.findByActivityId(activityId);
    }
}
