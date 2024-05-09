package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.VerificationRequest;
import com.guctechie.users.models.VerificationResult;
import com.guctechie.users.services.UserService;
import org.springframework.stereotype.Service;

@Service(CommandNames.VERIFY_EMAIL_COMMAND)
public class VerifyEmailCommand implements Command<VerificationRequest, VerificationResult> {

    private final UserService userService;

    public VerifyEmailCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public VerificationResult execute(VerificationRequest request) {
        logger.info("Executing verify email command for user: {}", request.getEmail());
        return userService.verifyEmail(request);
    }

    @Override
    public Class<VerificationRequest> getRequestType() {
        return VerificationRequest.class;
    }
}
