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
        return activityMapper.findById(id);
    }

    @Override
    public List<Activity> listAllActivities() {
        return activityMapper.findAll();
    }

    @Override
    public Activity createActivity(Activity activity) {
        validateActivityTime(activity, true);
        activityMapper.insert(activity);
        return activity;
    }

    @Override
    public Activity updateActivity(Activity activity) {
        validateActivityTime(activity, false);
        activityMapper.update(activity);
        return activity;
    }

    @Override
    public void deleteActivity(Integer id) {
        activityMapper.deleteById(id);
    }

    private void validateActivityTime(Activity activity, boolean enforceFutureStart) {
        if (activity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "æ´»åŠ¨ä¸èƒ½ä¸ºç©º");
        }
        LocalDateTime start = activity.getStartTime();
        LocalDateTime end = activity.getEndTime();
        LocalDateTime signupEnd = activity.getSignupEndTime();
        if (start == null || end == null || signupEnd == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "å¼€å§‹ã€ç»“æŸå’ŒæŠ¥åæˆªæ­¢æ—¶é—´å¿…é¡»å¡«å†™");
        }
        if (!end.isAfter(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ç»“æŸæ—¶é—´å¿…é¡»æ™šäºŽå¼€å§‹æ—¶é—´");
        }
        if (!signupEnd.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "æŠ¥åæˆªæ­¢æ—¶é—´å¿…é¡»æ—©äºŽå¼€å§‹æ—¶é—´");
        }
        LocalDateTime now = LocalDateTime.now();
        if (enforceFutureStart && !start.isAfter(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "å¼€å§‹æ—¶é—´å¿…é¡»æ™šäºŽå½“å‰æ—¶é—´");
        }
        if (enforceFutureStart && !signupEnd.isAfter(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "æŠ¥åæˆªæ­¢æ—¶é—´å¿…é¡»æ™šäºŽå½“å‰æ—¶é—´");
        }
    }
}
