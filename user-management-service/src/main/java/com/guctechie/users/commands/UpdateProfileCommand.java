package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.UpdateUserProfileRequest;
import com.guctechie.users.models.UpdateUserProfileResult;
import com.guctechie.users.models.UserProfileData;
import com.guctechie.users.services.UserProfileService;
import org.springframework.stereotype.Service;

@Service(value = CommandNames.UPDATE_USER)
public class UpdateProfileCommand implements Command<UpdateUserProfileRequest, UpdateUserProfileResult> {

    private final UserProfileService userProfileService;

    public UpdateProfileCommand(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }


    @Override
    public UpdateUserProfileResult execute(UpdateUserProfileRequest updateUserProfileRequest) {
        return userProfileService.updateProfile(updateUserProfileRequest);
    }

    @Override
    public Class<UpdateUserProfileRequest> getRequestType() {
        return UpdateUserProfileRequest.class;
    }
}
