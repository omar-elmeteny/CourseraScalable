package useractivity.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import useractivity.models.UserActivityLog;
import useractivity.repositories.UserActivityLogRepository;

import java.util.List;

@Service
public class UserActivityLogService {

    @Autowired
    private UserActivityLogRepository userActivityLogRepository;

    public void insertUserActivityLog(UserActivityLog userActivityLog) {
        userActivityLogRepository.save(userActivityLog);
    }

    public List<UserActivityLog> getUserActivityLogsOrdered(Integer userId) {
        return userActivityLogRepository.findByUserIdOrderedByActivityDateDesc(userId);
    }
    public void deleteUserActivityLog(Long logId) {
        userActivityLogRepository.deleteById(logId);
    }
}
