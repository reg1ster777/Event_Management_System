package com.program.programdesignb.controller;

import com.program.programdesignb.domain.Activity;
import com.program.programdesignb.service.ActivityService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activities")
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/{id}")
    public Activity getById(@PathVariable Integer id) {
        return activityService.getActivityById(id);
    }

    @GetMapping
    public List<Activity> listAll() {
        return activityService.listAllActivities();
    }

    @PostMapping
    public Activity create(@RequestBody Activity activity) {
        return activityService.createActivity(activity);
    }

    @PutMapping("/{id}")
    public Activity update(@PathVariable Integer id, @RequestBody Activity activity) {
        activity.setActivityId(id);
        return activityService.updateActivity(activity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id, @RequestParam("requesterId") Integer requesterId) {
        activityService.deleteActivity(id, requesterId);
    }
}
