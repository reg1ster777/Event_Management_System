package com.program.programdesignb.controller;

import com.program.programdesignb.domain.Registration;
import com.program.programdesignb.service.RegistrationService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activities/{activityId}/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public Registration register(@PathVariable Integer activityId, @RequestBody Registration registration) {
        return registrationService.register(activityId, registration);
    }

    @GetMapping
    public List<Registration> list(@PathVariable Integer activityId) {
        return registrationService.listByActivity(activityId);
    }
}
