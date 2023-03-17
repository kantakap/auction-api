package com.kantakap.auction.service;

import com.kantakap.auction.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Service
public interface UserService {

    Mono<User> me(Principal principal);

    Mono<User> save(User user);
    Mono<User> findByUsername(String username);
}
