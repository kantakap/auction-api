package com.kantakap.auction.model;

import com.kantakap.auction.security.domain.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Document
@Data
public class User {

    @Id
    private String id;
    private String username;
    private Boolean active;
    private Set<GrantedAuthority> roles = new HashSet<>();

    @Builder
    public User(String username) {
        this.username = username;
        this.active = true;
        this.roles.add(new SimpleGrantedAuthority(Role.ROLE_USER.toString()));
    }
}
