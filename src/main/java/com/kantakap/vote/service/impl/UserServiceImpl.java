package com.kantakap.vote.service.impl;

import com.kantakap.vote.model.User;
import com.kantakap.vote.repository.UserRepository;
import com.kantakap.vote.service.UserService;
import com.kantakap.vote.event.UserEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserEventService userEventService;

    @Override
    public Mono<User> me(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .doOnNext(userEventService::emitEvent);
    }

    @Override
    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

//    @Override
//    public Mono<User> dummy() {
//        return userRepository.save(User.builder().username("dummy").build())
//                .doOnSuccess(user -> System.out.println("Do on success: " + user.getId() + "ID created"))
//                .doOnNext(user -> System.out.println("Do on next: " + user.getUsername()))
//                .then(Mono.error(new RuntimeException("Dummy error")));
//    }

    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
