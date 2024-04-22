package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.InsertUserProfileResult;
import com.guctechie.users.models.UserProfileData;
import com.guctechie.users.services.UserProfileService;
import org.springframework.stereotype.Service;

@Service(value = CommandNames.INSERT_PROFILE)
public class InsertProfileCommand implements Command<UserProfileData, InsertUserProfileResult> {
    private final UserProfileService userProfileService;

    public InsertProfileCommand(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Override
    public InsertUserProfileResult execute(UserProfileData insertUserProfileRequest) throws Exception {
        return userProfileService.insertUserProfile(insertUserProfileRequest);
    }

    @Override
    public Class<UserProfileData> getRequestType() {
        return UserProfileData.class;
    }
}
