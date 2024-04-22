package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.DeleteProfileByUserIdRequest;
import com.guctechie.users.models.DeleteProfileResult;
import com.guctechie.users.services.UserProfileService;
import org.springframework.stereotype.Service;

@Service(CommandNames.DELETE_PROFILE_BY_USER_ID)
public class DeleteProfileByUserIdCommand implements Command<DeleteProfileByUserIdRequest, DeleteProfileResult> {
    private final UserProfileService userProfileService;

    public DeleteProfileByUserIdCommand(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }


    @Override
    public DeleteProfileResult execute(DeleteProfileByUserIdRequest deleteProfileRequest) {
        return DeleteProfileResult.builder().successful(
                userProfileService.deleteUserProfile(deleteProfileRequest.getUserId())
        ).build();
    }

    @Override
    public Class<DeleteProfileByUserIdRequest> getRequestType() {
        return DeleteProfileByUserIdRequest.class;
    }
}
