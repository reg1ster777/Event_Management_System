package com.program.programdesignb.controller;

import com.program.programdesignb.domain.Registration;
import com.program.programdesignb.service.RegistrationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public Registration register(@RequestBody RegistrationRequest request) {
        Registration registration = registrationService.register(
                request.activityId, request.name, request.phone, request.college);
        if (registration == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Registration failed or duplicate");
        }
        return registration;
    }

    @GetMapping("/activity/{activityId}")
    public List<Registration> listByActivity(@PathVariable Integer activityId) {
        return registrationService.listByActivity(activityId);
    }

    public static class RegistrationRequest {
        public Integer activityId;
        public String name;
        public String phone;
        public String college;
    }
}
