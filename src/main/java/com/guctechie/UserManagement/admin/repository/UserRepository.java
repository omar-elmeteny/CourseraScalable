package com.guctechie.UserManagement.admin.repository;

import com.guctechie.UserManagement.admin.model.UserModel;
import com.guctechie.UserManagement.admin.types.CheckUserResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Map;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    @Modifying
    @Transactional
    @Query(value = "CALL public.edit_user_profile(null,null,:buyerId,:firstName,:lastName,:email,:phone,:birthdate,:address,:password)", nativeQuery = true)
    Map<String, Object> updateUser(
            @Param("buyerId") Integer buyerId,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("birthdate") Date birthdate,
            @Param("address") String address,
            @Param("password") String password);


    @Query(value = "CALL public.get_user_profile(:buyerId)", nativeQuery = true)
    Map<String, Object> getUserProfile(@Param("buyerId") Integer buyerId);

    // insert user
    @Modifying
    @Transactional
    @Query(value = "CALL public.insert_user(:username,:email,:passwordHash,:fullName,:dateOfBirth,:phoneNumber)", nativeQuery = true)
    Map<String, Object> insertUser(
            @Param("username") String username,
            @Param("email") String email,
            @Param("passwordHash") String passwordHash,
            @Param("fullName") String fullName,
            @Param("dateOfBirth") Date dateOfBirth,
            @Param("phoneNumber") String phoneNumber);


    // check user if exists by email or username

    @Query(value = "CALL check_user_exists(:username, :email)", nativeQuery = true)
    CheckUserResult checkUserExists(
            @Param("username") String username,
            @Param("email") String email);



}
