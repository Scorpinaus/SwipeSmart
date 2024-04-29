package com.fdmgroup.apmproject.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fdmgroup.apmproject.model.User;

/**
 * This class represents a user's security details.
 * 
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
public class UserSecurityDetails implements UserDetails {
    private User user;

    /**
     * Constructs a UserSecurityDetails object with the specified user.
     *
     * @param user The user.
     */
    public UserSecurityDetails(User user) {
        super();
        this.user = user;
    }

    /**
     * Returns the authorities granted to the user.
     *
     * @return A collection of GrantedAuthority objects.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        return Arrays.asList(authority);
    }

    /**
     * Returns the user's password.
     *
     * @return The user's password.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the user's username.
     *
     * @return The user's username.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return True if the account has expired, false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     *
     * @return True if the account is locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials have expired.
     *
     * @return True if the credentials have expired, false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is enabled.
     *
     * @return True if the account is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Returns the user associated with these security details.
     *
     * @return The user.
     */
    public User getUser() {
        return user;
    }
}