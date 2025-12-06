package com.program.programdesignb.domain;

import java.time.LocalDateTime;

public class Registration {
    private Integer regId;
    private Integer activityId;
    private String name;
    private String phone;
    private String college;
    private LocalDateTime regTime;

    public Registration() {}

    public Registration(Integer regId, Integer activityId, String name, String phone, String college, LocalDateTime regTime) {
        this.regId = regId;
        this.activityId = activityId;
        this.name = name;
        this.phone = phone;
        this.college = college;
        this.regTime = regTime;
    }

    public Integer getRegId() { return regId; }
    public void setRegId(Integer regId) { this.regId = regId; }

    public Integer getActivityId() { return activityId; }
    public void setActivityId(Integer activityId) { this.activityId = activityId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public LocalDateTime getRegTime() { return regTime; }
    public void setRegTime(LocalDateTime regTime) { this.regTime = regTime; }

    @Override
    public String toString() {
        return "Registration{" +
                "regId=" + regId +
                ", activityId=" + activityId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
