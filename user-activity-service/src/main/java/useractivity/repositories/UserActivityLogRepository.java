package useractivity.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import useractivity.models.UserActivityLog;

public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {

    @Query(value = "SELECT * FROM get_user_activity_logs(:userId)"
            , countQuery = "SELECT count(*) FROM get_user_activity_logs(:userId)"
            , nativeQuery = true)
    Page<UserActivityLog> getUserActivityLogs(@Param("userId") int userId, Pageable pageable);

    //@Query(value = "SELECT * FROM find_by_user_id_ordered_by_activity_date_desc(:userId, :page, :size)", nativeQuery = true)
    @Query(value = "SELECT * FROM find_by_user_id_ordered_by_activity_date_desc(:userId)",
            countQuery = "SELECT count(*) FROM find_by_user_id_ordered_by_activity_date_desc(:userId)",
            nativeQuery = true)
    Page<UserActivityLog> findByUserIdOrderedByActivityDateDesc(
            @Param("userId") int userId,
            Pageable pageable
    );

}
