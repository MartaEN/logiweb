package com.marta.logistika.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserDetailsManager userDetailsManager;
    private final static GrantedAuthority DRIVER_AUTHORITY = new SimpleGrantedAuthority("ROLE_DRIVER");
    private final static GrantedAuthority NO_AUTHORITY = new SimpleGrantedAuthority("ROLE_NONE");

    @Autowired
    public SecurityServiceImpl(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ensureUserWithDriverRole(String username) {
        if (userDetailsManager.userExists(username)) {
            UserDetails currentUser = userDetailsManager.loadUserByUsername(username);
            Set<GrantedAuthority> authorities = new HashSet<>(currentUser.getAuthorities());
            if (!authorities.contains(DRIVER_AUTHORITY)) {
                authorities.add(DRIVER_AUTHORITY);
                authorities.remove(NO_AUTHORITY);
                UserDetails updatedUser = new User (username, currentUser.getPassword(), currentUser.isEnabled(), currentUser.isAccountNonExpired(), currentUser.isCredentialsNonExpired(), currentUser.isAccountNonLocked(), authorities);
                userDetailsManager.updateUser(updatedUser);
            }
        } else {
            // password = '123456' for all new users
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(DRIVER_AUTHORITY);
            UserDetails newUser = new User(username, "{bcrypt}$2a$10$iv5ShgUqjhK7exBn0zkhIuDXnombek7tIbsvbsnEfXrtFKdSPgqzS", authorities);
            userDetailsManager.createUser(newUser);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeDriverRoleFromUser(String username) {
        if (userDetailsManager.userExists(username)) {
            UserDetails currentUser = userDetailsManager.loadUserByUsername(username);
            Set<GrantedAuthority> authorities = new HashSet<>(currentUser.getAuthorities());
            authorities.remove(DRIVER_AUTHORITY);
            if (authorities.size() == 0) authorities.add(NO_AUTHORITY);
            UserDetails updatedUser = new User (username, currentUser.getPassword(), currentUser.isEnabled(), currentUser.isAccountNonExpired(), currentUser.isCredentialsNonExpired(), currentUser.isAccountNonLocked(), authorities);
            userDetailsManager.updateUser(updatedUser);
        }
    }
}
