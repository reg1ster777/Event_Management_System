package com.program.programdesignb.service.impl;

import com.program.programdesignb.domain.Registration;
import com.program.programdesignb.mapper.ActivityMapper;
import com.program.programdesignb.mapper.RegistrationMapper;
import com.program.programdesignb.service.RegistrationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationMapper registrationMapper;
    private final ActivityMapper activityMapper;

    public RegistrationServiceImpl(RegistrationMapper registrationMapper, ActivityMapper activityMapper) {
        this.registrationMapper = registrationMapper;
        this.activityMapper = activityMapper;
    }

    @Override
    public Registration register(Integer activityId, String name, String phone, String college) {
        if (activityId == null || activityMapper.findById(activityId) == null) {
            return null;
        }
        Registration existing = registrationMapper.findByActivityIdAndPhone(activityId, phone);
        if (existing != null) {
            return null;
        }
        Registration registration = new Registration();
        registration.setActivityId(activityId);
        registration.setName(name);
        registration.setPhone(phone);
        registration.setCollege(college);
        registrationMapper.insert(registration);
        return registration;
    }

    @Override
    public List<Registration> listByActivity(Integer activityId) {
        return registrationMapper.findByActivityId(activityId);
    }
}
