package com.kantakap.auction.unit.validator;

import com.kantakap.auction.validator.AuctionValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuctionValidatorTest {

    @Test
    @DisplayName("Should return true when minimum team size is less than maximum team size")
    void shouldReturnTrueWhenMinimumTeamSizeIsLessThanMaximumTeamSize() {
        // given
        var minimumTeamSize = 1;
        var maximumTeamSize = 2;

        // when
        var result = AuctionValidator.validateMinimumTeamSizeLessThanMaximumTeamSize(minimumTeamSize, maximumTeamSize);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when minimum team size is greater than maximum team size")
    void shouldReturnFalseWhenMinimumTeamSizeIsGreaterThanMaximumTeamSize() {
        // given
        var minimumTeamSize = 2;
        var maximumTeamSize = 1;

        // when
        var result = AuctionValidator.validateMinimumTeamSizeLessThanMaximumTeamSize(minimumTeamSize, maximumTeamSize);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return true when start time is after now.")
    void shouldReturnTrueWhenStartTimeIsAfterNow() {
        // given
        var startsAt = LocalDateTime.now().plusHours(1);

        // when
        var result = AuctionValidator.validateStartTimeIsBeforeNow(startsAt);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when start time is before now.")
    void shouldReturnFalseWhenStartTimeIsBeforeNow() {
        // given
        var startsAt = LocalDateTime.now().minusHours(1);

        // when
        var result = AuctionValidator.validateStartTimeIsBeforeNow(startsAt);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when start time is at least 30 minutes from now.")
    void shouldReturnFalseWhenStartTimeIsAtLeast30MinutesFromNow() {
        // given
        var startsAt = LocalDateTime.now().plusMinutes(29);

        // when
        var result = AuctionValidator.validateStartTimeLessThanHalfHour(startsAt);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return true when start time is at least 30 minutes from now.")
    void shouldReturnTrueWhenStartTimeIsAtLeast30MinutesFromNow() {
        // given
        var startsAt = LocalDateTime.now().plusMinutes(31);

        // when
        var result = AuctionValidator.validateStartTimeLessThanHalfHour(startsAt);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when maximum bid amount is greater than initial balance.")
    void shouldReturnFalseWhenMaximumBidAmountIsGreaterThanInitialBalance() {
        // given
        var maximumBidAmount = 100;
        var initialBalance = 50;

        // when
        var result = AuctionValidator.validateMaximumBidAmountLessThanInitialBalance(maximumBidAmount, initialBalance);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return true when maximum bid amount is less than initial balance.")
    void shouldReturnTrueWhenMaximumBidAmountIsLessThanInitialBalance() {
        // given
        var maximumBidAmount = 50;
        var initialBalance = 100;

        // when
        var result = AuctionValidator.validateMaximumBidAmountLessThanInitialBalance(maximumBidAmount, initialBalance);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when maximum bid amount is greater than initial balance * funds loss prevention percentage.")
    void shouldReturnFalseWhenMaximumBidAmountIsGreaterThanInitialBalanceTimesFundsLossPreventionPercentage() {
        // given
        var maximumBidAmount = 100;
        var initialBalance = 50;
        var fundsLossPreventionPercentage = 50;

        // when
        var result = AuctionValidator.canBidMaxAmount(maximumBidAmount, initialBalance, fundsLossPreventionPercentage);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return true when maximum bid amount is less than initial balance * funds loss prevention percentage.")
    void shouldReturnTrueWhenMaximumBidAmountIsLessThanInitialBalanceTimesFundsLossPreventionPercentage() {
        // given
        var maximumBidAmount = 25;
        var initialBalance = 50;
        var fundsLossPreventionPercentage = 50;

        // when
        var result = AuctionValidator.canBidMaxAmount(maximumBidAmount, initialBalance, fundsLossPreventionPercentage);

        // then
        assertTrue(result);
    }

}