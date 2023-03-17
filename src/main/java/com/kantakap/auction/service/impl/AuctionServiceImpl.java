package com.kantakap.auction.service.impl;

import com.kantakap.auction.model.Auction;
import com.kantakap.auction.model.AuctionStatus;
import com.kantakap.auction.model.User;
import com.kantakap.auction.payload.CreateAuction;
import com.kantakap.auction.repository.AuctionRepository;
import com.kantakap.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;

    @Override
    public Mono<Auction> createAuction(User creator, CreateAuction createAuction) {
        // validate start time
        var now = LocalDateTime.now();
        if (createAuction.getStartsAt().isBefore(now)) {
            return Mono.error(new IllegalArgumentException("Auction cannot start in the past."));
        } else if (createAuction.getStartsAt().minusMinutes(30L).isBefore(now)) {
            return Mono.error(new IllegalArgumentException("Auction cannot start in less than 30 minutes."));
        }

        // validate maximum bid amount
        if (createAuction.getMaximumBidAmount() > createAuction.getInitialBalance()) {
            return Mono.error(new IllegalArgumentException("Maximum bid amount cannot be greater than initial balance."));
        }

        // validate funds loss prevention to be able to max bid with initial balance
        var fundsLossPreventionPercentage = createAuction.getFundsLossPreventionPercentage() / 100;
        var maxPossibleBidWithInitialBalance = createAuction.getInitialBalance() * fundsLossPreventionPercentage;
        if (createAuction.getMaximumBidAmount() > maxPossibleBidWithInitialBalance) {
            return Mono.error(new IllegalArgumentException("Maximum bid amount cannot be greater than initial balance * funds loss prevention percentage."));
        }

        var auction = Auction.builder()
                .createdBy(creator)
                .title(createAuction.getTitle())
                .startsAt(createAuction.getStartsAt())
                .status(AuctionStatus.CREATED)
                .initialBalance(createAuction.getInitialBalance())
                .initialTime(createAuction.getInitialTime())
                .resetTime(createAuction.getResetTime())
                .maximumBidAmount(createAuction.getMaximumBidAmount())
                .minimumBidIncrement(createAuction.getMinimumBidIncrement())
                .build();
        return auctionRepository.save(auction)
                .log();
    }
}
