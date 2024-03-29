package com.kantakap.auction.validator;

import java.time.LocalDateTime;

public class AuctionValidator {

    /**
     * Validate that the minimum team size is less than the maximum team size
     * @param minimumTeamSize minimum team size
     * @param maximumTeamSize maximum team size
     * @return true if the minimum team size is less than the maximum team size, false otherwise
     */
    public static boolean validateMinimumTeamSizeLessThanMaximumTeamSize(int minimumTeamSize, int maximumTeamSize) {
        return minimumTeamSize < maximumTeamSize;
    }

    /**
     * Validate that the auction starts at a time in the future
     *
     * @param startsAt the time the auction starts
     * @return true if the auction starts in the future, false otherwise
     */
    public static boolean validateStartTimeIsBeforeNow(LocalDateTime startsAt) {
        var now = LocalDateTime.now();
        return !startsAt.isBefore(now);
    }

    /**
     * Validate that the auction starts at least 30 minutes from now
     *
     * @param startsAt the time the auction starts
     * @return true if the auction starts at least 30 minutes from now, false otherwise
     */
    public static boolean validateStartTimeLessThanHalfHour(LocalDateTime startsAt) {
        var now = LocalDateTime.now();
        var halfHourFromNow = startsAt.minusMinutes(30);
        return !halfHourFromNow.isBefore(now);
    }

    /**
     * Validate that the maximum bid amount is less than the initial balance
     *
     * @param maximumBidAmount the maximum bid amount
     * @param initialBalance   the initial balance
     * @return true if the maximum bid amount is less than the initial balance, false otherwise
     */
    public static boolean validateMaximumBidAmountLessThanInitialBalance(int maximumBidAmount, int initialBalance) {
        return maximumBidAmount <= initialBalance;
    }

    /**
     * Validate that the maximum bid amount is less than the initial balance * funds loss prevention percentage
     *
     * @param maximumBidAmount              the maximum bid amount
     * @param initialBalance                the initial balance
     * @param fundsLossPreventionPercentage the funds loss prevention percentage
     * @return true if the maximum bid amount is less than the initial balance * funds loss prevention percentage, false otherwise
     */
    public static boolean canBidMaxAmount(
            int maximumBidAmount,
            int initialBalance,
            int fundsLossPreventionPercentage
    ) {
        var maxPossibleBidWithInitialBalance = initialBalance * (fundsLossPreventionPercentage / 100d);
        return maximumBidAmount <= maxPossibleBidWithInitialBalance;
    }
}
