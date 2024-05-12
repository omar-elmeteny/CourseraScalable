package com.bugbusters.contentservice.commands;

import com.bugbusters.contentservice.messages.*;
import com.bugbusters.contentservice.messages.services.Command;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.bugbusters.contentservice.kafka_models.*;

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
