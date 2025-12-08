package com.program.programdesignb.service;

import com.program.programdesignb.domain.Checkin;
import java.util.List;

public interface CheckinService {
    Checkin doCheckin(Integer regId, String location);

    List<Checkin> listByRegId(Integer regId);
}
