package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.FilterUsersRequest;
import com.guctechie.users.models.ProfilesResult;
import com.guctechie.users.services.UserProfileService;
import org.springframework.stereotype.Service;

@Service(value = CommandNames.FILTER_PROFILES)
public class FilterProfilesCommand implements Command<FilterUsersRequest, ProfilesResult> {
    private final UserProfileService userProfileService;

    public FilterProfilesCommand(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }


    @Override
    public ProfilesResult execute(FilterUsersRequest request) {
        return userProfileService.findAllUsersByFilters(
                request
        );
    }

    @Override
    public Class<FilterUsersRequest> getRequestType() {
        return FilterUsersRequest.class;
    }
}
