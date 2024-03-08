package com.guctechie.UserManagement.admin.service;

import com.guctechie.UserManagement.admin.dto.AdminRegisterationDTO;
import com.guctechie.UserManagement.admin.repository.UserRepository;
import com.guctechie.UserManagement.admin.types.CheckUserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;


@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void callInsertUserStoredProcedure(AdminRegisterationDTO user) {

        CheckUserResult userResult= userRepository.checkUserExists(user.getUsername(), user.getEmail());

        if(userResult.isUserExists() ){
            throw new RuntimeException(userResult.getMessage());
        }

        String encodedPassword = passwordEncoder.encode(user.getPasswordUnhashed());
        userRepository.insertUser(user.getUsername(), user.getEmail(), encodedPassword, user.getFullName(), (Date) user.getDateOfBirth(), user.getPhoneNumber());

    }
}
