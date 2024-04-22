package com.guctechie.web.users.services;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.exceptions.MessageQueueException;
import com.guctechie.messages.services.CommandDispatcher;
import com.guctechie.users.models.UserByNameRequest;
import com.guctechie.users.models.UserByNameResult;
import com.guctechie.web.users.models.UserSecurityDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final CommandDispatcher commandDispatcher;

    public UsersService(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    public UserDetails loadUserByUsername(String username) {
        UserByNameResult result;
        try {
            result = commandDispatcher.sendCommand(CommandNames.GET_USER_BY_NAME,
                    UserByNameRequest.builder()
                            .username(username)
                            .build()
                    ,
                    UserByNameResult.class);
        } catch (MessageQueueException e) {
            throw new RuntimeException(e);
        }

        if (result == null) {
            return null;
        }
        final var details = result.getUserInfo();

        return new UserSecurityDetails(details);
    }

}
