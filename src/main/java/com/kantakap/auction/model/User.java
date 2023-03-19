package com.kantakap.auction.model;

import com.kantakap.auction.security.domain.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Document
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    private String id;
    private String username;

    private Long osuId;
    private Boolean active;
    private Set<GrantedAuthority> roles = new HashSet<>();

    public User(String username) {
        this.username = username;
        this.osuId = -1L;
        this.active = true;
        this.roles.add(new SimpleGrantedAuthority(Role.ROLE_USER.toString()));
    }

    public User(String username, Long osuId) {
        this.username = username;
        this.osuId = osuId;
        this.active = true;
        this.roles.add(new SimpleGrantedAuthority(Role.ROLE_USER.toString()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User user) {
            return user.getId().equals(this.getId());
        }
        return false;
    }
}
