package com.program.programdesignb.service.impl;

import com.program.programdesignb.domain.Activity;
import com.program.programdesignb.mapper.ActivityMapper;
import com.program.programdesignb.service.ActivityService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ActivityServiceImpl implements ActivityService {
    private final ActivityMapper activityMapper;
    private static final String STATUS_DRAFT = "DRAFT";
    private static final String STATUS_OPEN = "OPEN";
    private static final String STATUS_CLOSED = "CLOSED";
    private static final String STATUS_FINISHED = "FINISHED";
    private static final String STATUS_DELETED = "DELETED";

    public ActivityServiceImpl(ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }

    @Override
    public Activity getActivityById(Integer id) {
        return refreshStatus(activityMapper.findById(id));
    }

    @Override
    public List<Activity> listAllActivities() {
        List<Activity> list = activityMapper.findAll();
        if (list == null) return null;
        for (int i = 0; i < list.size(); i++) {
            list.set(i, refreshStatus(list.get(i)));
        }
        return list;
    }

    @Override
    public Activity createActivity(Activity activity) {
        if (activity.getStatus() == null || activity.getStatus().isBlank()) {
            activity.setStatus(STATUS_DRAFT);
        } else {
            activity.setStatus(normalizeStatus(activity.getStatus()));
        }
        validateActivityTime(activity, true);
        activityMapper.insert(activity);
        return activity;
    }

    @Override
    public Activity updateActivity(Activity activity) {
        validateActivityTime(activity, false);
        activityMapper.update(activity);
        return refreshStatus(activityMapper.findById(activity.getActivityId()));
    }

    @Override
    public void deleteActivity(Integer id, Integer requesterId) {
        Activity existing = activityMapper.findById(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "活动不存在");
        }
        if (requesterId == null || existing.getCreatedBy() == null
                || !existing.getCreatedBy().equals(requesterId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "仅允许删除自己创建的活动");
        }
        activityMapper.updateStatusById(id, STATUS_DELETED);
    }

    private void validateActivityTime(Activity activity, boolean enforceFutureStart) {
        if (activity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "活动不能为空");
        }
        LocalDateTime start = activity.getStartTime();
        LocalDateTime end = activity.getEndTime();
        LocalDateTime signupEnd = activity.getSignupEndTime();
        if (start == null || end == null || signupEnd == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "开始、结束和报名截止时间必须填写");
        }
        if (!end.isAfter(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "结束时间必须晚于开始时间");
        }
        if (!signupEnd.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "报名截止时间必须早于开始时间");
        }
        LocalDateTime now = LocalDateTime.now();
        if (enforceFutureStart && !start.isAfter(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "开始时间必须晚于当前时间");
        }
        if (enforceFutureStart && !signupEnd.isAfter(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "报名截止时间必须晚于当前时间");
        }
    }

    /**
     * 自动根据时间流转状态码。
     */
    private Activity refreshStatus(Activity activity) {
        if (activity == null) return null;
        String originalStatus = activity.getStatus();
        String current = normalizeStatus(originalStatus);
        if (current != null && !current.equals(originalStatus)) {
            activityMapper.updateStatusById(activity.getActivityId(), current);
            activity.setStatus(current);
        }
        if (STATUS_DELETED.equals(current)) {
            return activity;
        }
        LocalDateTime now = LocalDateTime.now();
        String newStatus = current;
        if (activity.getEndTime() != null && now.isAfter(activity.getEndTime())) {
            newStatus = STATUS_FINISHED;
        } else if (activity.getSignupEndTime() != null && now.isAfter(activity.getSignupEndTime())) {
            newStatus = STATUS_CLOSED;
        }
        if (newStatus != null && !newStatus.equals(current)) {
            activityMapper.updateStatusById(activity.getActivityId(), newStatus);
            activity.setStatus(newStatus);
        }
        return activity;
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return status;
        }
        return switch (status) {
            case "未发布" -> STATUS_DRAFT;
            case "报名中" -> STATUS_OPEN;
            case "已截止" -> STATUS_CLOSED;
            case "已结束" -> STATUS_FINISHED;
            case "已删除" -> STATUS_DELETED;
            default -> status;
        };
    }
}
