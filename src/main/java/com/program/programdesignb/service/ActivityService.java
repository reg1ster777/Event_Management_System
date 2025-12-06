package com.program.programdesignb.service;

import com.program.programdesignb.domain.Activity;
import java.util.List;

public interface ActivityService {
    Activity getActivityById(Integer id);

    List<Activity> listAllActivities();

    Activity createActivity(Activity activity);

    Activity updateActivity(Activity activity);

    void deleteActivity(Integer id);
}
