package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.ChangePasswordRequest;
import com.guctechie.users.models.ChangePasswordResult;
import com.guctechie.users.services.UserService;
import org.springframework.stereotype.Service;

@Service(value = CommandNames.CHANGE_PASSWORD_COMMAND)
public class ChangePasswordCommand implements Command<ChangePasswordRequest, ChangePasswordResult> {

    private final UserService userService;

    public ChangePasswordCommand(UserService authenticationService) {
        this.userService = authenticationService;
    }

    @Override
    public ChangePasswordResult execute(ChangePasswordRequest request) {
        logger.info("Executing change password command for user: {}", request.getUsername());
        return userService.changePassword(request);
    }

    @Override
    public Class<ChangePasswordRequest> getRequestType() {
        return ChangePasswordRequest.class;
    }
}
