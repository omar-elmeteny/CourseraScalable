package com.guctechie.UserManagement.admin.types;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CheckUserResult {
    private boolean userExists;
    private String message;

}
