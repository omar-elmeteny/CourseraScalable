package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.UpdateUserProfileResult;
import com.guctechie.users.models.UserProfileData;
import com.guctechie.users.services.UserProfileService;
import org.springframework.stereotype.Service;

@Service(value = CommandNames.UPDATE_PROFILE)
public class UpdateProfileCommand implements Command<UserProfileData, UpdateUserProfileResult> {

    private final UserProfileService userProfileService;

    public UpdateProfileCommand(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }


    @Override
    public UpdateUserProfileResult execute(UserProfileData userProfileData) {
        return userProfileService.updateProfile(userProfileData.getProfileId(), userProfileData);
    }

    @Override
    public Class<UserProfileData> getRequestType() {
        return UserProfileData.class;
    }
}
