package com.kantakap.vote.resolver;

import com.kantakap.vote.model.Auction;
import com.kantakap.vote.payload.CreateAuction;
import com.kantakap.vote.service.AuctionService;
import com.kantakap.vote.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuctionResolver {
    private final AuctionService auctionService;
    private final UserService userService;

    @MutationMapping
//    @PreAuthorize("hasRole('ROLE_USER')")
    public Mono<Auction> createAuction(Principal principal, @Validated @Argument CreateAuction createAuction) {
        return userService.me(principal)
                .flatMap(user -> auctionService.createAuction(user, createAuction))
                .log();
    }
}
