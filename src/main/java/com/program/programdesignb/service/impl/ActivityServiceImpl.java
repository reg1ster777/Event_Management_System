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
            activity.setStatus("未发布");
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
        activityMapper.updateStatusById(id, "已删除");
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
     * 按规则自动流转状态：
     * - 已删除：保持
     * - 结束时间已过：已结束
     * - 报名截止时间已过：已截止
     * 其余保持原状态（未发布/报名中等）。
     */
    private Activity refreshStatus(Activity activity) {
        if (activity == null) return null;
        String current = activity.getStatus();
        if ("已删除".equals(current)) {
            return activity;
        }
        LocalDateTime now = LocalDateTime.now();
        String newStatus = current;
        if (activity.getEndTime() != null && now.isAfter(activity.getEndTime())) {
            newStatus = "已结束";
        } else if (activity.getSignupEndTime() != null && now.isAfter(activity.getSignupEndTime())) {
            newStatus = "已截止";
        }
        if (!newStatus.equals(current)) {
            activityMapper.updateStatusById(activity.getActivityId(), newStatus);
            activity.setStatus(newStatus);
        }
        return activity;
    }
}
