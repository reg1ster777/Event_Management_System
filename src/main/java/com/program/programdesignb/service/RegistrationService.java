package com.program.programdesignb.service;

import com.program.programdesignb.domain.Registration;
import java.util.List;

public interface RegistrationService {
    Registration register(Integer activityId, Registration registration);

    List<Registration> listByActivity(Integer activityId);
}
