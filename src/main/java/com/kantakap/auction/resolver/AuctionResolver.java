package com.kantakap.auction.resolver;

import com.kantakap.auction.model.Auction;
import com.kantakap.auction.payload.CreateAuction;
import com.kantakap.auction.quartz.QuartzTestSample;
import com.kantakap.auction.service.AuctionService;
import com.kantakap.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuctionResolver {
    private final AuctionService auctionService;
    private final UserService userService;
    private final QuartzTestSample quartzTestSample;

    /**
     * Create a new auction
     * @param principal the user
     * @param createAuction the auction to create
     * @return the created auction
     */
    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Mono<Auction> createAuction(Principal principal, @Validated @Argument CreateAuction createAuction) {
        return userService.me(principal)
                .flatMap(user -> auctionService.createAuction(user, createAuction))
                .log();
    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Mono<Auction> processPlayersData(Principal principal, @Argument String auctionId) {
        return userService.me(principal)
                .map(user -> auctionService.findAuctionById(auctionId)
                        .filter(auction -> auctionService.isAuctionCreator(user, auction))
                        .switchIfEmpty(Mono.error(new RuntimeException("You are not the creator of this auction"))))
                .flatMap(auction -> auctionService.processPlayersData(auctionId));
    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Mono<Auction> startAuction(Principal principal, @Argument String auctionId) {
        return userService.me(principal)
                .flatMap(user -> auctionService.startAuction(user, auctionId))
                .log();
    }

    @MutationMapping
    public Mono<Auction> stopAuction(@Argument String auctionId) {
        quartzTestSample.stopJob(auctionId);
        return null;
    }
}
