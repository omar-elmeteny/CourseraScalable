package useractivity.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import useractivity.models.UserActivityLog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserActivityLogRepository {

    @Autowired
    private final JdbcTemplate jdbcTemplate;




    public Page<UserActivityLog> getUserActivityLogs(int userId, Pageable pageable) {
        String sql = "SELECT * FROM find_user_activity_logs(?)";
        String countSql = "SELECT count(*) FROM find_user_activity_logs(?)";

        Object[] params = { userId };

        // Assuming UserActivityLogMapper is a RowMapper for UserActivityLog, adjust it as needed
        List<UserActivityLog> userActivityLogs = jdbcTemplate.query(
                sql,
                params,
                new UserActivityLogMapper()
        );

        long totalCount = jdbcTemplate.queryForObject(countSql, params, Long.class);

        // Implement logic to paginate the results and return as a Page
        // This depends on your database type and version
        // For simplicity, you can return the entire list without pagination for now
        return new PageImpl<>(userActivityLogs, pageable, totalCount);
    }

    public Page<UserActivityLog> findByUserIdOrderedByActivityDateDesc(Integer userId, Pageable pageable) {
        String sql = "SELECT * FROM find_by_user_id_ordered_by_activity_date_desc(?)";
        String countSql = "SELECT count(*) FROM find_by_user_id_ordered_by_activity_date_desc(?)";

        Object[] params = { userId };

        // Assuming UserActivityLogMapper is a RowMapper for UserActivityLog, adjust it as needed
        List<UserActivityLog> userActivityLogs = jdbcTemplate.query(
                sql,
                params,
                new UserActivityLogMapper()
        );

        long totalCount = jdbcTemplate.queryForObject(countSql, params, Long.class);

        // Implement logic to paginate the results and return as a Page
        // This depends on your database type and version
        // For simplicity, you can return the entire list without pagination for now
        return new PageImpl<>(userActivityLogs, pageable, totalCount);
    }

    // Assuming UserActivityLogMapper is a RowMapper for UserActivityLog, adjust it as needed
    private static class UserActivityLogMapper implements RowMapper<UserActivityLog> {
        @Override
        public UserActivityLog mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            // Implement mapping logic based on your UserActivityLog entity
            // Adjust column names and types accordingly
            return new UserActivityLog(
                    resultSet.getLong("log_id"),
                    resultSet.getLong("user_id"),
                    resultSet.getString("activity_type"),
                    resultSet.getString("activity_description"),
                    resultSet.getTimestamp("activity_date").toLocalDateTime()

            );
        }
    }

    public UserActivityLog saveUserActivityLog(UserActivityLog userActivityLog) {
        String sql = "INSERT INTO user_activity_log (user_id, activity_type, activity_description, activity_date) " +
                "VALUES (?, ?, ?, ?)";

        Object[] params = {
                userActivityLog.getUserId(),
                userActivityLog.getActivityType(),
                userActivityLog.getActivityDescription(),
                userActivityLog.getActivityDate()
        };

        int[] types = {
                Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
        };

        // Execute the INSERT query
        jdbcTemplate.update(sql, params, types);

        // Assuming your UserActivityLog entity has an ID field, set it based on the generated ID
        userActivityLog.setLogId(jdbcTemplate.queryForObject("SELECT lastval()", Long.class));

        return userActivityLog;
    }

    public void deleteUserActivityLog(Long logId) {
        String sql = "DELETE FROM user_activity_log WHERE id = ?";

        Object[] params = { logId };

        // Execute the DELETE query
        jdbcTemplate.update(sql, params);
    }
}














//package useractivity.repositories;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import useractivity.models.UserActivityLog;
//
//public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {
//
//    @Query(value = "SELECT * FROM get_user_activity_logs(:userId)"
//            , countQuery = "SELECT count(*) FROM get_user_activity_logs(:userId)"
//            , nativeQuery = true)
//    Page<UserActivityLog> getUserActivityLogs(@Param("userId") int userId, Pageable pageable);
//
//    //@Query(value = "SELECT * FROM find_by_user_id_ordered_by_activity_date_desc(:userId, :page, :size)", nativeQuery = true)
//    @Query(value = "SELECT * FROM find_by_user_id_ordered_by_activity_date_desc(:userId)",
//            countQuery = "SELECT count(*) FROM find_by_user_id_ordered_by_activity_date_desc(:userId)",
//            nativeQuery = true)
//    Page<UserActivityLog> findByUserIdOrderedByActivityDateDesc(
//            @Param("userId") int userId,
//            Pageable pageable
//    );
//
//}
