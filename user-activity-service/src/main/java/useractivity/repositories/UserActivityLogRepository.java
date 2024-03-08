package useractivity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import useractivity.models.UserActivityLog;

import java.util.List;

public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM find_by_user_id_ordered_by_activity_date_desc(:p_user_id)")
    List<UserActivityLog> findByUserIdOrderedByActivityDateDesc(@Param("p_user_id") Long userId);

}
