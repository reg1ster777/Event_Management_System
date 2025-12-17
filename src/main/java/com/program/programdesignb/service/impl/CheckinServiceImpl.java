package com.program.programdesignb.service.impl;

import com.program.programdesignb.domain.Checkin;
import com.program.programdesignb.domain.Registration;
import com.program.programdesignb.mapper.CheckinMapper;
import com.program.programdesignb.mapper.RegistrationMapper;
import com.program.programdesignb.service.CheckinService;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CheckinServiceImpl implements CheckinService {
    private final CheckinMapper checkinMapper;
    private final RegistrationMapper registrationMapper;

    public CheckinServiceImpl(CheckinMapper checkinMapper, RegistrationMapper registrationMapper) {
        this.checkinMapper = checkinMapper;
        this.registrationMapper = registrationMapper;
    }

    @Override
    public Checkin doCheckin(Integer regId, String location) {
        Registration registration = registrationMapper.findById(regId);
        if (registration == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "报名记录不存在");
        }

        Checkin checkin = new Checkin();
        checkin.setRegId(regId);
        checkin.setActivityId(registration.getActivityId());
        checkin.setStudentNo(registration.getStudentNo());
        checkin.setName(registration.getName());
        checkin.setPhone(registration.getPhone());
        checkin.setLocation(location);
        checkinMapper.insert(checkin);
        return checkin;
    }

    @Override
    public List<Checkin> listByRegId(Integer regId) {
        Registration registration = registrationMapper.findById(regId);
        if (registration == null) {
            return Collections.emptyList();
        }
        return checkinMapper.findByActivityAndStudent(registration.getActivityId(), registration.getStudentNo());
    }

    @Override
    public List<Checkin> listByActivityId(Integer activityId) {
        return checkinMapper.findByActivityId(activityId);
    }
}
