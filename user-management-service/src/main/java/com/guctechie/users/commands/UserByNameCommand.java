package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.UserByNameRequest;
import com.guctechie.users.models.UserByNameResult;
import com.guctechie.users.models.UserProfileData;
import com.guctechie.users.services.UserService;
import org.springframework.stereotype.Service;

@Service(value = CommandNames.GET_USER_BY_NAME)
public class UserByNameCommand implements Command<UserByNameRequest, UserByNameResult> {
    private final UserService userService;

    public UserByNameCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserByNameResult execute(UserByNameRequest userByNameRequest) {
        logger.info("Executing get user by name command for user: {}", userByNameRequest.getUsername());
        UserProfileData user = userService.findUserByUsername(userByNameRequest.getUsername());
        if (user == null) {
            return new UserByNameResult();
        }
        return UserByNameResult.builder()
                .user(UserProfileData.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phoneNumber(user.getPhoneNumber())
                        .dateOfBirth(user.getDateOfBirth())
                        .profilePhotoUrl(user.getProfilePhotoUrl())
                        .userId(user.getUserId())
                        .build())
                .build();
    }

    @Override
    public Class<UserByNameRequest> getRequestType() {
        return UserByNameRequest.class;
    }
}
