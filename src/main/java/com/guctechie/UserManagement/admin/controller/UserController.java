package com.guctechie.UserManagement.admin.controller;


import com.guctechie.UserManagement.admin.dto.AdminRegisterationDTO;
import com.guctechie.UserManagement.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public  String createUser(@RequestBody AdminRegisterationDTO user) {
         userService.callInsertUserStoredProcedure(user);
         return "success";
    }


}
