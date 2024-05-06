package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.LockAccountResult;
import com.guctechie.users.models.UnlockAccountRequest;
import com.guctechie.users.services.UserService;
import org.springframework.stereotype.Service;

@Service(CommandNames.UNLOCK_ACCOUNT)
public class UnlockAccountCommand implements Command<UnlockAccountRequest, LockAccountResult> {

    private final UserService userService;

    public UnlockAccountCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public LockAccountResult execute(UnlockAccountRequest request) {
        return userService.unlockAccount(request);
    }

    @Override
    public Class<UnlockAccountRequest> getRequestType() {
        return UnlockAccountRequest.class;
    }
}
