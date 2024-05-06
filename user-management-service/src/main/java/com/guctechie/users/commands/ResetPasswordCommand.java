package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.ChangePasswordResult;
import com.guctechie.users.models.ResetPasswordRequest;
import com.guctechie.users.services.UserService;
import org.springframework.stereotype.Service;

@Service(CommandNames.RESET_PASSWORD)
public class ResetPasswordCommand implements Command<ResetPasswordRequest, ChangePasswordResult> {

    private final UserService userService;

    public ResetPasswordCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ChangePasswordResult execute(ResetPasswordRequest request) {
        return userService.resetPassword(request);
    }

    @Override
    public Class<ResetPasswordRequest> getRequestType() {
        return ResetPasswordRequest.class;
    }
}
