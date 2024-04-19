package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.RegistrationRequest;
import com.guctechie.users.models.RegistrationResult;
import com.guctechie.users.services.UserService;
import org.springframework.stereotype.Service;

@Service(value = CommandNames.REGISTER_COMMAND)
public class RegistrationCommand implements Command<RegistrationRequest, RegistrationResult> {
    private final UserService userService;

    public RegistrationCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public RegistrationResult execute(RegistrationRequest registrationRequest) {
        return userService.registerUser(registrationRequest);
    }

    @Override
    public Class<RegistrationRequest> getRequestType() {
        return RegistrationRequest.class;
    }
}
