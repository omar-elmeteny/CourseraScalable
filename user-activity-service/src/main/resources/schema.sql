
-- Table to store user activity logs
CREATE TABLE IF NOT EXISTS user_activity_logs(
    log_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    activity_type VARCHAR(50) NOT NULL CHECK (activity_type IN ('login', 'logout', 'profile_update', 'password_change', 'email_verification', 'phone_verification', 'role_update', 'course_enrollment', 'course_completion', 'event_registration', 'event_participation', 'social_media_link')),
    activity_description TEXT NOT NULL,
    activity_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



