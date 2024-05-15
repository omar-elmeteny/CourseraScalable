package com.bugbusters.course.commands;

import org.springframework.stereotype.Service;

import com.bugbusters.course.messages.*;
import com.bugbusters.course.messages.services.Command;
import com.bugbusters.course.kafka_models.*;

@Service(value = CommandNames.CHANGE_PASSWORD_COMMAND)
public class ChangePasswordCommand implements Command<ChangePasswordRequest, ChangePasswordResult> {

    public ChangePasswordCommand() {
    }

    @Override
    public ChangePasswordResult execute(ChangePasswordRequest request) {
        logger.info("Executing change password command for user: {}", request.getUsername());
        return null;
    }

    @Override
    public Class<ChangePasswordRequest> getRequestType() {
        return ChangePasswordRequest.class;
    }
}
