package useractivity.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import useractivity.models.UserActivityLog;
import useractivity.services.UserActivityLogService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-activity-logs")
public class UserActivityLogController {

    @Autowired
    private UserActivityLogService userActivityLogService;

    @PostMapping
    public void insertUserActivityLog(@RequestBody UserActivityLog userActivityLog) {
        userActivityLogService.insertUserActivityLog(userActivityLog);
    }

    @GetMapping("/{userId}")
    public List<UserActivityLog> getUserActivityLogs(@PathVariable Integer userId) {
        return userActivityLogService.getUserActivityLogsOrdered(userId);
    }

    @DeleteMapping("/{logId}")
    public void deleteUserActivityLog(@PathVariable Long logId) {
        userActivityLogService.deleteUserActivityLog(logId);
    }
}
