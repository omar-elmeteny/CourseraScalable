package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.DeleteAdminRequest;
import com.guctechie.users.models.DeleteResult;
import com.guctechie.users.services.UserProfileService;
import org.springframework.stereotype.Service;

@Service(CommandNames.DELETE_ADMIN)
public class DeleteAdminCommand implements Command<DeleteAdminRequest, DeleteResult> {

    private final UserProfileService userProfileService;

    public DeleteAdminCommand(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Override
    public DeleteResult execute(DeleteAdminRequest deleteProfileRequest) {
        logger.info("Executing delete admin command for user: {}", deleteProfileRequest.getUsername());
        return userProfileService.deleteAdmin(deleteProfileRequest.getUsername(), deleteProfileRequest.getUserId());
    }

    @Override
    public Class<DeleteAdminRequest> getRequestType() {
        return DeleteAdminRequest.class;
    }
}
