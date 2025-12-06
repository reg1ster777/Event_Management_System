package com.program.programdesignb.service.impl;

import com.program.programdesignb.domain.Checkin;
import com.program.programdesignb.mapper.CheckinMapper;
import com.program.programdesignb.service.CheckinService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CheckinServiceImpl implements CheckinService {
    private final CheckinMapper checkinMapper;

    public CheckinServiceImpl(CheckinMapper checkinMapper) {
        this.checkinMapper = checkinMapper;
    }

    @Override
    public Checkin doCheckin(Integer regId, String location) {
        Checkin checkin = new Checkin();
        checkin.setRegId(regId);
        checkin.setLocation(location);
        checkinMapper.insert(checkin);
        return checkin;
    }

    @Override
    public List<Checkin> listByRegId(Integer regId) {
        return checkinMapper.findByRegId(regId);
    }
}
