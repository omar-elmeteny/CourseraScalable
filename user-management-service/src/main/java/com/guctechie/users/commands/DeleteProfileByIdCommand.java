package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.DeleteProfileRequest;
import com.guctechie.users.models.DeleteProfileResult;
import com.guctechie.users.services.UserProfileService;
import org.springframework.stereotype.Service;

@Service(CommandNames.DELETE_PROFILE_BY_ID)
public class DeleteProfileByIdCommand implements Command<DeleteProfileRequest, DeleteProfileResult> {

    private final UserProfileService userProfileService;

    public DeleteProfileByIdCommand(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }


    @Override
    public DeleteProfileResult execute(DeleteProfileRequest deleteProfileRequest) throws Exception {
        return DeleteProfileResult.builder().successful(
                userProfileService.deleteUserProfile(deleteProfileRequest.getProfileId())
        ).build();
    }

    @Override
    public Class<DeleteProfileRequest> getRequestType() {
        return DeleteProfileRequest.class;
    }
}
