package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.ForgotPasswordRequest;
import com.guctechie.users.models.ForgotPasswordResult;
import com.guctechie.users.services.UserService;
import org.springframework.stereotype.Service;

@Service(CommandNames.FORGOT_PASSWORD)
public class ForgotPasswordCommand implements Command<ForgotPasswordRequest, ForgotPasswordResult> {

    private final UserService userService;

    public ForgotPasswordCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ForgotPasswordResult execute(ForgotPasswordRequest request) {
        return userService.forgotPassword(request);
    }

    @Override
    public Class<ForgotPasswordRequest> getRequestType() {
        return ForgotPasswordRequest.class;
    }
}
