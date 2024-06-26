package com.guctechie.datainsertions.services;

import com.guctechie.datainsertions.exceptions.ApplicationException;
import com.guctechie.datainsertions.models.User;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserInserter {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(UserInserter.class);

    public UserInserter(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public int insertUser(User user) throws ApplicationException {

        logger.trace("Inserting user {} into the database", user.getUsername());
        String sql = """
                INSERT INTO public.users
                (username, email, password_hash, full_name, date_of_birth, registration_date, is_email_verified, is_phone_verified, profile_photo_url, phone_number)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING user_id;
                """;

        String socialMediaSql = """
                INSERT INTO public.social_media_info
                (user_id, social_media_name, social_media_link)
                VALUES (?, ?, ?);
                """;
        String userRoleSql = """
                INSERT INTO public.user_roles
                (user_id, role_id)
                VALUES (?, ?);
                """;
        String userLoginHistorySql = """
                INSERT INTO public.login_history
                (user_id, login_status, timestamp, ip_address, user_agent)
                VALUES (?, ?, ?, ?, ?);
                """;
        String userProblemReportSql = """
                INSERT INTO public.problem_reports
                (user_id, problem_description, status, reported_at)
                VALUES (?, ?, ?, ?);
                """;
        String userPasswordResetRequestSql = """
                INSERT INTO public.password_reset_requests
                (user_id, reset_token, request_date)
                VALUES (?, ?, ?);
                """;
        String userActivityLogSql = """
                INSERT INTO public.user_activity_logs
                (user_id, activity_description, activity_date)
                VALUES (?, ?, ?);
                """;

//        profile_id SERIAL PRIMARY KEY,
//        user_id INT REFERENCES users(user_id),
//                first_name VARCHAR(50) NOT NULL,
//        last_name VARCHAR(50) NOT NULL,
//        bio TEXT,
//        profile_photo_url VARCHAR(255),
//                is_email_verified BOOLEAN NOT NULL DEFAULT FALSE,
//                is_phone_verified BOOLEAN NOT NULL DEFAULT FALSE,
//                phone_number VARCHAR(15) UNIQUE,
//                date_of_birth DATE NOT NULL,
//                unique (user_id)

        String user_profile_sql = """
                INSERT INTO public.user_profile
                (user_id, first_name, last_name, is_email_verified, is_phone_verified, phone_number, date_of_birth)
                VALUES (?, ?, ?, ?, ?, ?, ?);
                """;





        String encodedPassword = passwordEncoder.encode(user.getPassword());
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, user.getUsername(), user.getEmail(), encodedPassword, user.getFullName(), user.getDateOfBirth(), user.getRegistrationDate(), user.isEmailVerified(), user.isPhoneVerified(), user.getProfilePhotoUrl(), user.getPhoneNumber());
        if (rs.next()) {
            int id = rs.getInt(1);
            logger.trace("User {} inserted with id {}", user.getUsername(), id);

            user.getSocialMediaLinks().forEach((k, v) -> jdbcTemplate.update(socialMediaSql, id, k, v));
            user.getRoleId().forEach(role -> jdbcTemplate.update(userRoleSql, id, role));
            user.getHistory().forEach(history -> jdbcTemplate.update(userLoginHistorySql, id, history.getValue0(), history.getValue1(), history.getValue2(), history.getValue3()));
            user.getProblemReports().forEach(problem -> jdbcTemplate.update(userProblemReportSql, id, problem.getValue0(), problem.getValue1(), problem.getValue2()));
            user.getPasswordResetRequests().forEach(request -> {
                String hashedToken = passwordEncoder.encode(request.getValue0());
                jdbcTemplate.update(userPasswordResetRequestSql, id, hashedToken, request.getValue1());
            });
            user.getUserActivityLogs().forEach(log -> jdbcTemplate.update(userActivityLogSql, id, log.getValue0(), log.getValue1()));

            jdbcTemplate.update(user_profile_sql, id, user.getFullName().split(" ")[0], user.getFullName().split(" ")[1], user.isEmailVerified(), user.isPhoneVerified(), user.getPhoneNumber(), user.getDateOfBirth());
            return id;
        }

        logger.error("Error inserting user {}", user.getUsername());
        throw new ApplicationException("Error inserting user" + user.getUsername());
    }
}
