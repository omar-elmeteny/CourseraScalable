package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.UserByNameRequest;
import com.guctechie.users.models.UserByNameResult;
import com.guctechie.users.models.UserDetails;
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
        UserDetails userDetails = userService.findUserByUsername(userByNameRequest.getUsername());
        if (userDetails == null) {
            return new UserByNameResult();
        }
        return UserByNameResult.builder()
                .userDetails(userDetails)
                .build();
    }

    @Override
    public Class<UserByNameRequest> getRequestType() {
        return UserByNameRequest.class;
    }
}
