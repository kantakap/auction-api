package com.kantakap.auction.security.domain;

import com.kantakap.auction.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserPrincipal implements OAuth2User, UserDetails {

    private String username;
    private Long osuId;

    private Boolean active;
    private Set<GrantedAuthority> roles = new HashSet<>();
    private Map<String, Object> attributes;

    public static UserPrincipal create(User user) {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setUsername(user.getUsername());
        userPrincipal.setOsuId(user.getOsuId());
        userPrincipal.setActive(user.getActive());
        userPrincipal.setRoles(user.getRoles());
        return userPrincipal;
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.active;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setOsuId(Long osuId) {
        this.osuId = osuId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<GrantedAuthority> getRoles() {
        return roles;
    }

    public void setRoles(Set<GrantedAuthority> roles) {
        this.roles = roles;
    }

    @Override
    public String getName() {
        return this.username;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
