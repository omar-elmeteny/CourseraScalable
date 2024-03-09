package useractivity.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import useractivity.models.UserActivityLog;
import useractivity.services.UserActivityLogService;

@RestController
@RequestMapping("/api/v1/user-activity-logs")
public class UserActivityLogController {

    @Autowired
    private final UserActivityLogService userActivityLogService;


    public UserActivityLogController(UserActivityLogService userActivityLogService) {
        this.userActivityLogService = userActivityLogService;
    }

    @GetMapping
    public Page<UserActivityLog> getUserActivityLogs(
            @RequestParam int userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userActivityLogService.getUserActivityLogs(userId, page, size);
    }

    @GetMapping("/find-ordered")
    public Page<UserActivityLog> findByUserIdOrderedByActivityDateDesc(
            @RequestParam(required = false) Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userActivityLogService.findByUserIdOrderedByActivityDateDesc(userId, page, size);
    }

    @PostMapping
    public UserActivityLog saveUserActivityLog(@RequestBody UserActivityLog userActivityLog) {
        return userActivityLogService.saveUserActivityLog(userActivityLog);
    }

    @DeleteMapping("/{logId}")
    public void deleteUserActivityLog(@PathVariable Long logId) {
        userActivityLogService.deleteUserActivityLog(logId);
    }
}
