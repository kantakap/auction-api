package com.kantakap.vote.resolver;

import com.kantakap.vote.model.User;
import com.kantakap.vote.service.UserService;
import com.kantakap.vote.event.UserEventService;
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
//    @MessageMapping("graphql")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public Flux<User> userEvent(Principal principal) {
//        ReactiveSecurityContextHolder.getContext().doOnNext(context -> log.info("User {} subscribed to userEvent", principal.getName())).subscribe();
        log.info("Principal {} subscribed to userEvent", principal);
        return userEventService.subscribe();
    }
}
