package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.ProfileByUserIdRequest;
import com.guctechie.users.models.UserProfileData;
import com.guctechie.users.services.UserProfileService;
import org.springframework.stereotype.Service;

@Service( CommandNames.GET_PROFILE_BY_USER_ID)
public class ProfileByUserIdCommand implements Command<ProfileByUserIdRequest, UserProfileData> {
    private final UserProfileService userProfileService;

    public ProfileByUserIdCommand(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Override
    public UserProfileData execute(ProfileByUserIdRequest profileByUserIdRequest) throws Exception {
        return userProfileService.findUserProfileByUserId(profileByUserIdRequest.getUserId());
    }

    @Override
    public Class<ProfileByUserIdRequest> getRequestType() {
        return ProfileByUserIdRequest.class;
    }
}
