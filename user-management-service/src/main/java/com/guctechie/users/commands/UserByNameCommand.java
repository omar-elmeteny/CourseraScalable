package com.guctechie.users.commands;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.Command;
import com.guctechie.users.models.UserByNameRequest;
import com.guctechie.users.models.UserByNameResult;
import com.guctechie.users.models.UserInfo;
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
        UserInfo userInfo = userService.findUserByUsername(userByNameRequest.getUsername());
        if (userInfo == null) {
            return new UserByNameResult();
        }
        return UserByNameResult.builder()
                .userInfo(userInfo)
                .build();
    }

    @Override
    public Class<UserByNameRequest> getRequestType() {
        return UserByNameRequest.class;
    }
}
