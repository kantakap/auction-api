package com.kantakap.vote.service.impl;

import com.kantakap.vote.model.Auction;
import com.kantakap.vote.model.AuctionStatus;
import com.kantakap.vote.model.User;
import com.kantakap.vote.payload.CreateAuction;
import com.kantakap.vote.repository.AuctionRepository;
import com.kantakap.vote.service.AuctionService;
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
        } else if (createAuction.getStartsAt().plusMinutes(30L).isBefore(now)) {
            return Mono.error(new IllegalArgumentException("Auction cannot start more than 30 minutes in the future."));
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
