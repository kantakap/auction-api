package com.kantakap.auction.resolver;

import com.kantakap.auction.model.Bid;
import com.kantakap.auction.service.BidService;
import com.kantakap.auction.service.BidderService;
import com.kantakap.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BidResolver {
    private final BidService bidService;
    private final BidderService bidderService;
    private final UserService userService;

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Mono<Bid> bid(Principal principal, String auctionId, Integer amount) {
        var now = LocalDateTime.now();
        return userService.me(principal)
                .flatMap(bidderService::getBidderFromUser)
                .flatMap(bidder -> bidService.bid(bidder, auctionId, amount, now));
    }
}
