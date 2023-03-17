package com.kantakap.auction.resolver;

import com.kantakap.auction.model.User;
import com.kantakap.auction.service.UserService;
import com.kantakap.auction.event.UserEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserResolver {
    private final UserService userService;
    private final UserEventService userEventService;

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Mono<User> me(Principal principal) {
        return userService.me(principal);
    }

    @SubscriptionMapping
//    @PreAuthorize("hasRole('ROLE_USER')")
    public Flux<User> userEvent(Principal principal) {
        log.info("Principal {} subscribed to userEvent", principal);
        return userEventService.subscribe();
    }
}
