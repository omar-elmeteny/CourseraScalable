package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.AuthenticationRequest;
import com.guctechie.users.models.AuthenticationResult;
import com.guctechie.users.services.UserService;
import org.springframework.stereotype.Service;

@Service(value = CommandNames.LOGIN_COMMAND)
public class LoginCommand implements Command<AuthenticationRequest, AuthenticationResult> {

    private final UserService userService;

    public LoginCommand(UserService authenticationService) {
        this.userService = authenticationService;
    }


    @Override
    public AuthenticationResult execute(AuthenticationRequest request) {
        var result = userService.isAuthenticUser(request.getUsername(), request.getPassword(), request.getIpAddress(), request.getUserAgent());
        return AuthenticationResult.builder()
                .authenticated(result == null)
                .username(result == null ?  request.getUsername() : null)
                .message(result)
                .build();
    }

    @Override
    public Class<AuthenticationRequest> getRequestType() {
        return AuthenticationRequest.class;
    }
}
