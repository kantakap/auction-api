package com.kantakap.auction.resolver;

import com.kantakap.auction.model.User;
import com.kantakap.auction.security.TokenProvider;
import com.kantakap.auction.security.domain.UserPrincipal;
import com.kantakap.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Profile("dev")
@RestController
@Slf4j
@RequiredArgsConstructor
public class TestUserResolver {
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @QueryMapping
    public Mono<String> getTestToken() {
        return userService.findByOsuId(-1L)
                .switchIfEmpty(userService.save(new User("Test user", -1L)))
                .flatMap(user -> {
                    UserPrincipal userPrincipal = UserPrincipal.create(user);
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            userPrincipal,
                            null,
                            user.getRoles()
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    return Mono.just(tokenProvider.generate(auth));
                });
    }
}
