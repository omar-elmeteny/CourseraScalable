package com.guctechie.datainsertions.services;

import com.guctechie.datainsertions.exceptions.ApplicatioException;
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

    public int insertUser(User user) throws ApplicatioException {

        logger.trace("Inserting user {} into the database", user.getUsername());
        String sql = """
                INSERT INTO public.users
                (username, email, password_hash, full_name, date_of_birth, registration_date, is_email_verified, is_phone_verified, profile_photo_url, phone_number)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING user_id;
                """;
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, user.getUsername(), user.getEmail(), encodedPassword, user.getFullName(), user.getDateOfBirth(), user.getRegistrationDate(), user.isEmailVerified(), user.isPhoneVerified(), user.getProfilePhotoUrl(), user.getPhoneNumber());
        if (rs.next()) {
            int id = rs.getInt(1);
            logger.trace("User {} inserted with id {}", user.getUsername(), id);
            return id;
        }

        logger.error("Error inserting user {}", user.getUsername());
        throw new ApplicatioException("Error inserting user" + user.getUsername());
    }
}
