package com.kantakap.vote.resolver;

import com.kantakap.vote.model.Auction;
import com.kantakap.vote.payload.CreateAuction;
import com.kantakap.vote.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuctionResolver {
    private final AuctionService auctionService;

    @MutationMapping
//    @PreAuthorize("hasRole('ROLE_USER')")
    public Mono<Auction> createAuction(Principal principal, @Argument CreateAuction createAuction) {
        return auctionService.createAuction(createAuction);
    }
}
