package com.kantakap.auction.service.impl;

import com.kantakap.auction.model.Auction;
import com.kantakap.auction.model.AuctionStatus;
import com.kantakap.auction.model.User;
import com.kantakap.auction.payload.CreateAuction;
import com.kantakap.auction.repository.AuctionRepository;
import com.kantakap.auction.service.AuctionService;
import com.kantakap.auction.validator.AuctionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;

    /**
     * Creates an auction.
     *
     * @param creator       User who created the auction.
     * @param createAuction Auction creation payload.
     * @return Mono of the created auction.
     */
    @Override
    public Mono<Auction> createAuction(User creator, CreateAuction createAuction) {
        if (creator == null)
            return Mono.error(new NullPointerException("Creator cannot be null."));
        if (createAuction == null)
            return Mono.error(new IllegalArgumentException("Create auction payload cannot be null."));

        if (!AuctionValidator.validateStartTimeIsBeforeNow(createAuction.getStartsAt()))
            return Mono.error(new IllegalArgumentException("Auction cannot start in the past."));

        if (!AuctionValidator.validateStartTimeLessThanHalfHour(createAuction.getStartsAt()))
            return Mono.error(new IllegalArgumentException("Auction cannot start in less than 30 minutes."));

        if (!AuctionValidator.validateMaximumBidAmountLessThanInitialBalance(createAuction.getMaximumBidAmount(), createAuction.getInitialBalance()))
            return Mono.error(new IllegalArgumentException("Maximum bid amount cannot be greater than initial balance."));

        if (!AuctionValidator.canBidMaxAmount(
                createAuction.getMaximumBidAmount(),
                createAuction.getInitialBalance(),
                createAuction.getFundsLossPreventionPercentage()))
            return Mono.error(new IllegalArgumentException("Maximum bid amount cannot be greater than initial balance * funds loss prevention percentage."));

        var auction = Auction.builder()
                .createdBy(creator)
                .title(createAuction.getTitle())
                .startsAt(createAuction.getStartsAt())
                .status(AuctionStatus.CREATED)
                .initialBalance(createAuction.getInitialBalance())
                .fundsLossPreventionPercentage(createAuction.getFundsLossPreventionPercentage())
                .initialTime(createAuction.getInitialTime())
                .resetTime(createAuction.getResetTime())
                .maximumBidAmount(createAuction.getMaximumBidAmount())
                .minimumBidIncrement(createAuction.getMinimumBidIncrement())
                .build();
        return auctionRepository.save(auction)
                .log();
    }
}
