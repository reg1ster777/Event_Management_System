package com.program.programdesignb.controller;

import com.program.programdesignb.domain.Registration;
import com.program.programdesignb.service.RegistrationService;
import com.program.programdesignb.util.TokenUtil;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/activities/{activityId}/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;
    private final TokenUtil tokenUtil;

    public RegistrationController(RegistrationService registrationService, TokenUtil tokenUtil) {
        this.registrationService = registrationService;
        this.tokenUtil = tokenUtil;
    }

    @PostMapping
    public Registration register(@PathVariable Integer activityId, @RequestBody RegistrationRequest request) {
        validateToken(activityId, request.token);
        Registration registration = request.toRegistration();
        return registrationService.register(activityId, registration);
    }

    @GetMapping
    public List<Registration> list(@PathVariable Integer activityId) {
        return registrationService.listByActivity(activityId);
    }

    private void validateToken(Integer activityId, String token) {
        if (!StringUtils.hasText(token) || !tokenUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "报名码已失效或无效");
        }
        Integer tokenActivityId = tokenUtil.getActivityIdFromToken(token);
        if (tokenActivityId == null || !tokenActivityId.equals(activityId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "报名码与活动不匹配");
        }
        String tokenType = tokenUtil.getTokenType(token);
        if (!"registration".equals(tokenType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "报名码类型错误");
        }
    }

    public static class RegistrationRequest {
        public String name;
        public String phone;
        public String school;
        public String college;
        public String clazz;
        public String studentNo;
        public String email;
        public String token;

        public Registration toRegistration() {
            Registration registration = new Registration();
            registration.setName(name);
            registration.setPhone(phone);
            registration.setSchool(school);
            registration.setCollege(college);
            registration.setClazz(clazz);
            registration.setStudentNo(studentNo);
            registration.setEmail(email);
            return registration;
        }
    }
}
