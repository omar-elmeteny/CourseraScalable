package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.ChangePasswordResult;
import com.guctechie.users.models.ResetUserPasswordRequest;
import com.guctechie.users.services.UserService;
import org.springframework.stereotype.Service;

@Service(CommandNames.RESET_USER_PASSWORD)
public class ResetUserPasswordCommand implements Command<ResetUserPasswordRequest, ChangePasswordResult> {

    private final UserService userService;

    public ResetUserPasswordCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ChangePasswordResult execute(ResetUserPasswordRequest request) {
        logger.info("Executing reset user password command for user: {}", request.getEmail());
        return userService.resetUserPassword(request);
    }

    @Override
    public Class<ResetUserPasswordRequest> getRequestType() {
        return ResetUserPasswordRequest.class;
    }
}
