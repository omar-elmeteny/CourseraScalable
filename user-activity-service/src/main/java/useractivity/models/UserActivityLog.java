package useractivity.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity_logs")
public class UserActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    private Long userId;
    private String activityType;
    private String activityDescription;
    private LocalDateTime activityDate;

    // Getters and setters

}
