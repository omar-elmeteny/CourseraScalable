package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.DeleteUserRequest;
import com.guctechie.users.models.DeleteResult;
import com.guctechie.users.services.UserProfileService;
import org.springframework.stereotype.Service;

@Service(CommandNames.DELETE_USER)
public class DeleteUserCommand implements Command<DeleteUserRequest, DeleteResult> {
    private final UserProfileService userProfileService;

    public DeleteUserCommand(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }


    @Override
    public DeleteResult execute(DeleteUserRequest deleteProfileRequest) {
        return userProfileService.deleteUser(deleteProfileRequest.getUsername());
    }

    @Override
    public Class<DeleteUserRequest> getRequestType() {
        return DeleteUserRequest.class;
    }
}
