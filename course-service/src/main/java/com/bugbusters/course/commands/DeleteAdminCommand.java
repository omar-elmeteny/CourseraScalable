package com.bugbusters.course.commands;

import com.bugbusters.course.messages.*;
import com.bugbusters.course.messages.services.Command;

import org.springframework.stereotype.Service;

import com.bugbusters.course.kafka_models.*;

@Service(CommandNames.DELETE_ADMIN)
public class DeleteAdminCommand implements Command<DeleteAdminRequest, DeleteResult> {

    public DeleteAdminCommand() {
    }

    @Override
    public DeleteResult execute(DeleteAdminRequest deleteProfileRequest) {
        logger.info("Executing delete admin command for user: {}", deleteProfileRequest.getUsername());
        return null;
    }

    @Override
    public Class<DeleteAdminRequest> getRequestType() {
        return DeleteAdminRequest.class;
    }
}
