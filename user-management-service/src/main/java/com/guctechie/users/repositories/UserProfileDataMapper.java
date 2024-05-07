package com.guctechie.users.repositories;

import com.guctechie.users.models.UserProfileData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileDataMapper implements RowMapper<UserProfileData> {
    public static final UserProfileDataMapper INSTANCE = new UserProfileDataMapper();
    private UserProfileDataMapper() {

    }
    @Override
    public UserProfileData mapRow(ResultSet rs, int rowNum) throws SQLException {
        return com.guctechie.users.models.UserProfileData.builder()
                .userId(rs.getInt("user_id"))
                .username(rs.getString("username"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .bio(rs.getString("bio"))
                .phoneNumber(rs.getString("phone_number"))
                .dateOfBirth(rs.getDate("date_of_birth"))
                .profilePhotoUrl(rs.getString("profile_photo_url"))
                .build();
    }
}
