package com.guctechie.web.users.services;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.exceptions.MessageQueueException;
import com.guctechie.messages.services.CommandDispatcher;
import com.guctechie.users.models.UserByNameRequest;
import com.guctechie.users.models.UserByNameResult;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

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
                    UserByNameRequest.builder().username(username), UserByNameResult.class);
        } catch (MessageQueueException e) {
            throw new RuntimeException(e);
        }

        if (result == null) {
            return null;
        }
        final var details = result.getUserDetails();

        return new UserDetails() {

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }

            @Override
            public String getPassword() {
                return details.getPasswordHash();
            }

            @Override
            public String getUsername() {
                return details.getUsername();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

}
