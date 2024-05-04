package com.guctechie.users.repositories;


import com.guctechie.users.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertUser(User user) {
        String sql = """
                SELECT * FROM register_user(?, ?, ?, ?, ?, ?, ?)
                
                """;
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql,
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getDateOfBirth(),
                user.getProfilePhotoUrl()
                );

        if (rs.next()) {
            user.setUserId(rs.getInt("user_id"));
            user.setRegistrationDate(rs.getDate("registration_date"));
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

    public ArrayList<String> getUserRoles(int userId) {
        String sql = """
                SELECT r.role_name FROM public.user_roles ur INNER JOIN public.roles r on r.role_id = ur.role_id WHERE ur.user_id = ?
                """;
        return new ArrayList<>(jdbcTemplate.queryForList(sql, String.class, userId));
    }

    public void updatePassword(User user) {
        String sql = """
                    UPDATE public.users SET password_hash = ? WHERE user_id = ?
        """;
        jdbcTemplate.update(sql, user.getPasswordHash(), user.getUserId());
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
