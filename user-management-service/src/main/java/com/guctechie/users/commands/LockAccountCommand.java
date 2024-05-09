package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.LockAccountRequest;
import com.guctechie.users.models.LockAccountResult;
import com.guctechie.users.services.UserService;
import org.springframework.stereotype.Service;

@Service(CommandNames.LOCK_ACCOUNT)
public class LockAccountCommand implements Command<LockAccountRequest, LockAccountResult> {

    private final UserService userService;

    public LockAccountCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public LockAccountResult execute(LockAccountRequest request) {
        logger.info("Executing lock account command for user: {}", request.getUserId());
        return userService.lockAccount(request);
    }

    @Override
    public Class<LockAccountRequest> getRequestType() {
        return LockAccountRequest.class;
    }
}
