package com.program.programdesignb.domain;

import java.time.LocalDateTime;

public class Checkin {
    private Integer checkinId;
    /**
     * 当前签到关联的报名ID，仅用于向外返回数据。
     */
    private Integer regId;
    private Integer activityId;
    private String studentNo;
    private String name;
    private String phone;
    private LocalDateTime checkinTime;
    private String location;

    public Checkin() {}

    public Integer getCheckinId() { return checkinId; }
    public void setCheckinId(Integer checkinId) { this.checkinId = checkinId; }

    public Integer getRegId() { return regId; }
    public void setRegId(Integer regId) { this.regId = regId; }

    public Integer getActivityId() { return activityId; }
    public void setActivityId(Integer activityId) { this.activityId = activityId; }

    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDateTime getCheckinTime() { return checkinTime; }
    public void setCheckinTime(LocalDateTime checkinTime) { this.checkinTime = checkinTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    @Override
    public String toString() {
        return "Checkin{" +
                "checkinId=" + checkinId +
                ", regId=" + regId +
                ", activityId=" + activityId +
                ", studentNo='" + studentNo + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
