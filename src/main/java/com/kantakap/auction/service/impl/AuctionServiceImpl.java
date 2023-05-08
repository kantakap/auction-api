package com.kantakap.auction.service.impl;

import com.kantakap.auction.model.Auction;
import com.kantakap.auction.model.AuctionStatus;
import com.kantakap.auction.model.Player;
import com.kantakap.auction.model.User;
import com.kantakap.auction.payload.CreateAuction;
import com.kantakap.auction.quartz.QuartzTestSample;
import com.kantakap.auction.repository.AuctionRepository;
import com.kantakap.auction.service.AuctionService;
import com.kantakap.auction.service.FileProcessorService;
import com.kantakap.auction.validator.AuctionValidator;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;
    private final QuartzTestSample quartzTestSample;
    private final FileProcessorService fileProcessorService;

    /**
     * Finds an auction by id.
     * @param auctionId ID of the auction.
     * @return Mono of the auction.
     */
    @Override
    public Mono<Auction> findAuctionById(String auctionId) {
        return auctionRepository.findById(auctionId);
    }

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
            return Mono.error(new NullPointerException("Create auction payload cannot be null."));

        if (!AuctionValidator.validateMinimumTeamSizeLessThanMaximumTeamSize(createAuction.getMinimumTeamSize(), createAuction.getMaximumTeamSize()))
            return Mono.error(new IllegalArgumentException("Minimum team size cannot be greater than maximum team size."));

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
                .minimumTeamSize(createAuction.getMinimumTeamSize())
                .maximumTeamSize(createAuction.getMaximumTeamSize())
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

    @Override
    public Mono<Auction> startAuction(User user, String auctionId) {
        return this.findAuctionById(auctionId)
                .flatMap(auction -> {
                    if (!auction.getCreatedBy().equals(user))
                        return Mono.error(new IllegalArgumentException("User is not the creator of the auction."));
                    if (!auction.getStatus().equals(AuctionStatus.CREATED))
                        return Mono.error(new IllegalArgumentException("Auction is not in the CREATED status."));
                    auction.setStatus(AuctionStatus.STARTED);
                    quartzTestSample.testQuartz(auction);
                    return auctionRepository.save(auction);
                });
    }

    /**
     * Checks if the user is the creator of the auction.
     * @param user User to check.
     * @param auction Auction to check.
     * @return True if the user is the creator of the auction, false otherwise.
     */
    @Override
    public boolean isAuctionCreator(User user, Auction auction) {
        return auction.getCreatedBy().getId().equals(user.getId());
    }
}
