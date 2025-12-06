package com.program.programdesignb.controller;

import com.program.programdesignb.domain.Checkin;
import com.program.programdesignb.service.CheckinService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkins")
public class CheckinController {
    private final CheckinService checkinService;

    public CheckinController(CheckinService checkinService) {
        this.checkinService = checkinService;
    }

    @PostMapping
    public Checkin doCheckin(@RequestBody CheckinRequest request) {
        return checkinService.doCheckin(request.regId, request.location);
    }

    @GetMapping("/registration/{regId}")
    public List<Checkin> listByReg(@PathVariable Integer regId) {
        return checkinService.listByRegId(regId);
    }

    public static class CheckinRequest {
        public Integer regId;
        public String location;
    }
}
