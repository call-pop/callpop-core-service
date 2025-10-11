package com.sdcompany.tadak.login.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class TadakUser implements UserDetails {
    private final Long id;
    private final String userIdentifier;
    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;

    public TadakUser(
            Long id,
            String userIdentifier,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.userIdentifier = userIdentifier;
        this.username = String.valueOf(id);
        this.authorities = authorities;
    }

    @Override public String getPassword() { return ""; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
