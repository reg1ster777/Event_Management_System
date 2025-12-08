package com.program.programdesignb.domain;

import java.time.LocalDateTime;

public class Activity {
    private Integer activityId;
    private String title;
    private String type;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private LocalDateTime signupEndTime;
    private Integer maxParticipants;
    private String status;
    private Integer createdBy;
    private LocalDateTime createdTime;

    public Activity() {}

    public Activity(Integer activityId, String title, String type, String description,
                    LocalDateTime startTime, LocalDateTime endTime, String location,
                    LocalDateTime signupEndTime, Integer maxParticipants, String status,
                    Integer createdBy, LocalDateTime createdTime) {
        this.activityId = activityId;
        this.title = title;
        this.type = type;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.signupEndTime = signupEndTime;
        this.maxParticipants = maxParticipants;
        this.status = status;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
    }

    public Integer getActivityId() { return activityId; }
    public void setActivityId(Integer activityId) { this.activityId = activityId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getSignupEndTime() { return signupEndTime; }
    public void setSignupEndTime(LocalDateTime signupEndTime) { this.signupEndTime = signupEndTime; }

    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }

    @Override
    public String toString() {
        return "Activity{" +
                "activityId=" + activityId +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
