package com.program.programdesignb.service;

import com.program.programdesignb.domain.Registration;
import java.util.List;

public interface RegistrationService {
    Registration register(Integer activityId, String name, String phone, String college);

    List<Registration> listByActivity(Integer activityId);
}
