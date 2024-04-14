package com.guctechie.messagequeue.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {
    private String jwt;
    private boolean success;
}
