package com.guctechie.web.users.models;

import com.guctechie.users.models.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserSecurityDetails implements UserDetails {
    private final UserInfo userInfo;

    public UserSecurityDetails(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userInfo.getRoles().stream().map(role -> (GrantedAuthority) () -> "ROLE_" + role.toUpperCase()).toList();
    }

    @Override
    public String getPassword() {
        return userInfo.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return userInfo.getUsername();
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
