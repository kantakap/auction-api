package com.kantakap.auction.security;

import com.kantakap.auction.security.domain.UserPrincipal;
import com.kantakap.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {
    private final UserService userService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userService.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                .map(user -> {
                    final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
                    UserDetails userDetails = UserPrincipal.create(user);
                    detailsChecker.check(userDetails);
                    return userDetails;
                });
    }
}
