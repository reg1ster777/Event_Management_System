package com.program.programdesignb.domain;

import java.time.LocalDateTime;

public class Checkin {
    private Integer checkinId;
    private Integer regId;
    private LocalDateTime checkinTime;
    private String location;

    public Checkin() {}

    public Checkin(Integer checkinId, Integer regId, LocalDateTime checkinTime, String location) {
        this.checkinId = checkinId;
        this.regId = regId;
        this.checkinTime = checkinTime;
        this.location = location;
    }

    public Integer getCheckinId() { return checkinId; }
    public void setCheckinId(Integer checkinId) { this.checkinId = checkinId; }

    public Integer getRegId() { return regId; }
    public void setRegId(Integer regId) { this.regId = regId; }

    public LocalDateTime getCheckinTime() { return checkinTime; }
    public void setCheckinTime(LocalDateTime checkinTime) { this.checkinTime = checkinTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    @Override
    public String toString() {
        return "Checkin{" +
                "checkinId=" + checkinId +
                ", regId=" + regId +
                ", location='" + location + '\'' +
                '}';
    }
}
