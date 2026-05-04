package com.bank.security.auth;

import com.bank.user.domain.Role;
import com.bank.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/// Adapter between entity and Spring Security
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    /// ROLE - GrantedAuthority
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    /// Spring Security use it as a password
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /// Spring Security use it as a username
    /// in project it's - login
    @Override
    public String getUsername() {
        return user.getLogin();
    }

    /// Checking if accout expired
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    ///Checking if an account is NOT locked
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

     ///Checking if password didn't expire
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /// checking if account if active
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }


    ///Here is an access to original User entity. Quite useful
    public User getUser() {
        return user;
    }

    public Long getUserId() {
        return user.getId();
    }

    public Role getRole() {
        return user.getRole();
    }
}
