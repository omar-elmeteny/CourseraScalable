package useractivity.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import useractivity.models.UserActivityLog;
import useractivity.repositories.UserActivityLogRepository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserActivityLogService {


    @Autowired
    private final UserActivityLogRepository userActivityLogRepository;



    public Page<UserActivityLog> getUserActivityLogs(int userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userActivityLogRepository.getUserActivityLogs(userId, pageRequest);
    }

    public Page<UserActivityLog> findByUserIdOrderedByActivityDateDesc(int userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userActivityLogRepository.findByUserIdOrderedByActivityDateDesc(userId, pageRequest);
    }
    public UserActivityLog saveUserActivityLog(UserActivityLog userActivityLog) {
        return userActivityLogRepository.save(userActivityLog);
    }

    public void deleteUserActivityLog(Long logId) {
        userActivityLogRepository.deleteById(logId);
    }

}
