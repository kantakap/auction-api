package com.kantakap.auction.service.impl;

import com.kantakap.auction.model.User;
import com.kantakap.auction.repository.UserRepository;
import com.kantakap.auction.service.UserService;
import com.kantakap.auction.event.UserEventService;
import graphql.GraphqlErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserEventService userEventService;

    /**
     * Get the current user
     * @param principal the user
     * @return the user
     */
    @Override
    public Mono<User> me(Principal principal) {
        if (principal == null)
            throw new GraphqlErrorException.Builder()
                    .message("JWT is missing. Not authenticated.")
                    .build();
        return userRepository.findByUsername(principal.getName())
                .doOnNext(userEventService::emitEvent);
    }

    /**
     * Save a user
     * @param user the user to save
     * @return the saved user
     */
    @Override
    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    /**
     * Find a user by username
     * @param username the username
     * @return the user
     */
    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Find a user by osu id
     * @param osuId the osu id
     * @return the user
     */
    @Override
    public Mono<User> findByOsuId(Long osuId) {
        return userRepository.findByOsuId(osuId);
    }
}
