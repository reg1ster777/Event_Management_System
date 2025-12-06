package com.program.programdesignb.service.impl;

import com.program.programdesignb.domain.Activity;
import com.program.programdesignb.mapper.ActivityMapper;
import com.program.programdesignb.service.ActivityService;
import java.util.List;
import org.springframework.stereotype.Service;

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
        activityMapper.insert(activity);
        return activity;
    }

    @Override
    public Activity updateActivity(Activity activity) {
        activityMapper.update(activity);
        return activity;
    }

    @Override
    public void deleteActivity(Integer id) {
        activityMapper.deleteById(id);
    }
}
