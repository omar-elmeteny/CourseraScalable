package com.guctechie.users.repositories;


import com.guctechie.users.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertUser(User user) {
        String sql = """
                INSERT INTO public.users
                (username,
                 email,
                 password_hash,
                 full_name,
                 date_of_birth,
                 registration_date,
                 is_email_verified,
                 is_phone_verified,
                 profile_photo_url,
                 phone_number)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING user_id;
                """;
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql,
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getFullName(),
                user.getDateOfBirth(),
                user.getRegistrationDate(),
                user.isEmailVerified(),
                user.isPhoneVerified(),
                user.getProfilePhotoUrl(),
                user.getPhoneNumber());

        if (rs.next()) {
            user.setUserId(rs.getInt("user_id"));
        }
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        return rowSet.next();
    }

    public boolean emailExists(String email) {
        String sql = "SELECT user_id FROM users WHERE email = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, email);
        return rowSet.next();
    }

    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), username);
        if (users.isEmpty()) {
            return null;
        }
        return users.stream().findFirst().get();
    }

    public static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    rs.getString("full_name"),
                    rs.getDate("date_of_birth"),
                    rs.getDate("registration_date"),
                    rs.getBoolean("is_email_verified"),
                    rs.getBoolean("is_phone_verified"),
                    rs.getString("profile_photo_url"),
                    rs.getString("phone_number")
            );
        }
    }

}
