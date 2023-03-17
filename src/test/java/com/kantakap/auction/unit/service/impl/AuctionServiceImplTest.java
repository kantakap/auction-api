package com.kantakap.auction.unit.service.impl;

import com.kantakap.auction.model.Auction;
import com.kantakap.auction.model.User;
import com.kantakap.auction.payload.CreateAuction;
import com.kantakap.auction.service.impl.AuctionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
class AuctionServiceImplTest {

    @InjectMocks
    AuctionServiceImpl auctionService;

    User user;
    CreateAuction createAuction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("Test user");
        createAuction = CreateAuction.builder()
                .title("Test auction")
                .startsAt(LocalDateTime.now().plusHours(1))
                .initialBalance(12000)
                .fundsLossPreventionPercentage(75)
                .initialTime(60)
                .resetTime(30)
                .maximumBidAmount(9000)
                .minimumBidIncrement(200)
                .build();
    }

    @Test
    @DisplayName("Should throw exception when User (auction creator) is null.")
    void shouldThrowExceptionWhenUserIsNull() {
        // given
        user = null;

        // when
        Mono<Auction> result = auctionService.createAuction(user, createAuction);

        // then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("Creator cannot be null."))
                .verify();
    }

    @Test
    @DisplayName("Should throw exception when Create Auction object is null.")
    void shouldThrowExceptionWhenCreateAuctionIsNull() {
        // given
        createAuction = null;

        // when
        Mono<Auction> result = auctionService.createAuction(user, createAuction);

        // then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("Create auction payload cannot be null."))
                .verify();
    }

    @Test
    @DisplayName("Should throw exception when start time is in past.")
    void shouldThrowExceptionWhenStartTimeIsInPast() {
        // given
        createAuction.setStartsAt(LocalDateTime.of(2020, 1, 1, 12, 0));

        // when
        Mono<Auction> result = auctionService.createAuction(user, createAuction);

        // then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("Auction cannot start in the past."))
                .verify();
    }

    @Test
    @DisplayName("Should throw exception when start time of the auction is less than 30 minutes from now.")
    void shouldThrowExceptionWhenStartTimeIsLessThan30MinutesFromNow() {
        // given
        createAuction.setStartsAt(LocalDateTime.now().plusMinutes(29));

        // when
        Mono<Auction> result = auctionService.createAuction(user, createAuction);

        // then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("Auction cannot start in less than 30 minutes."))
                .verify();
    }

    @Test
    @DisplayName("Should throw exception when maximum bid amount is greater than initial balance.")
    void shouldThrowExceptionWhenMaximumBidAmountIsLessThanInitialBalance() {
        // given
        createAuction.setMaximumBidAmount(9000);
        createAuction.setInitialBalance(8000);

        // when
        Mono<Auction> result = auctionService.createAuction(user, createAuction);

        // then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("Maximum bid amount cannot be greater than initial balance."))
                .verify();
    }

    @Test
    @DisplayName("Should throw exception when maximum bid is greater than initial balance * funds loss prevention percentage.")
    void shouldThrowExceptionWhenMaximumBidAmountIsGreaterThanInitialBalanceTimesFundsLossPreventionPercentage() {
        // given
        createAuction.setMaximumBidAmount(9000);
        createAuction.setInitialBalance(10000);
        createAuction.setFundsLossPreventionPercentage(75);

        // when
        Mono<Auction> result = auctionService.createAuction(user, createAuction);

        // then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("Maximum bid amount cannot be greater than initial balance * funds loss prevention percentage."))
                .verify();
    }

}