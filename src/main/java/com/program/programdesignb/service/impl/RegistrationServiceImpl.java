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
        sanitizeRegistration(registration);
        ensureRequiredFields(registration);

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
        if (activity.getEndTime() != null && now.isAfter(activity.getEndTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "该活动已结束，无法报名");
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

    private void sanitizeRegistration(Registration registration) {
        if (registration == null) return;
        registration.setName(trim(registration.getName()));
        registration.setPhone(trim(registration.getPhone()));
        registration.setStudentNo(trim(registration.getStudentNo()));
        registration.setSchool(trim(registration.getSchool()));
        registration.setCollege(trim(registration.getCollege()));
        registration.setClazz(trim(registration.getClazz()));
        registration.setEmail(trim(registration.getEmail()));
    }

    private void ensureRequiredFields(Registration registration) {
        if (registration == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "报名信息不能为空");
        }
        if (!StringUtils.hasText(registration.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "姓名为必填项");
        }
        if (!StringUtils.hasText(registration.getPhone())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "手机号为必填项");
        }
        if (!StringUtils.hasText(registration.getStudentNo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "学号为必填项");
        }
        if (!isDigits(registration.getPhone())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "手机号只能包含数字");
        }
        if (!isDigits(registration.getStudentNo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "学号只能包含数字");
        }
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isDigits(String value) {
        return value != null && value.matches("\\d+");
    }

    @Override
    public List<Registration> listByActivity(Integer activityId) {
        return registrationMapper.findByActivityId(activityId);
    }
}
