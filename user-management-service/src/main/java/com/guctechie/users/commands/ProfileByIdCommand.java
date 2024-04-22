package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.ProfileByIdRequest;
import com.guctechie.users.models.UserProfileData;
import com.guctechie.users.services.UserProfileService;
import org.springframework.stereotype.Service;

@Service( CommandNames.GET_PROFILE_PROFILE_ID)
public class ProfileByIdCommand implements Command<ProfileByIdRequest, UserProfileData> {
    private final UserProfileService userProfileService;

    public ProfileByIdCommand(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Override
    public UserProfileData execute(ProfileByIdRequest profileByIdRequest) throws Exception {
        return userProfileService.findUserProfileByProfileId(profileByIdRequest.getProfileId());
    }

    @Override
    public Class<ProfileByIdRequest> getRequestType() {
        return ProfileByIdRequest.class;
    }
}
