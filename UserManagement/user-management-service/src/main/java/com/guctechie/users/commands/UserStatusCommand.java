package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.UserStatusRequest;
import com.guctechie.users.models.UserStatusResult;
import com.guctechie.users.services.UserService;
import org.springframework.stereotype.Service;

@Service(CommandNames.USER_STATUS)
public class UserStatusCommand implements Command<UserStatusRequest, UserStatusResult> {

    private final UserService userService;

    public UserStatusCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserStatusResult execute(UserStatusRequest request) {
        logger.info("Executing user status command for user: {}", request.getUserId());
        return userService.getUserStatus(request);
    }

    @Override
    public Class<UserStatusRequest> getRequestType() {
        return UserStatusRequest.class;
    }
}
