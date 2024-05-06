package com.guctechie.web.users.models;

import com.guctechie.users.models.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserSecurityDetails implements UserDetails {
    private final UserStatus userStatus;

    public UserSecurityDetails(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userStatus.getRoles().stream().map(role -> (GrantedAuthority) () -> "ROLE_" + role.toUpperCase()).toList();
    }

    @Override
    public String getPassword() {
        return userStatus.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return userStatus.getUsername();
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
}
