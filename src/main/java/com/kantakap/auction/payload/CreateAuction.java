package com.kantakap.auction.payload;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateAuction {
    @NotNull(message = "Title must be specified")
    @NotBlank(message = "Title must be specified")
    private String title;
    @NotNull(message = "Start time must be specified")
    private LocalDateTime startsAt;

    @Min(value = 1000, message = "Initial balance must be at least 1000")
    private Integer initialBalance;

    @Min(value = 50, message = "Funds loss prevention percentage must be at least 50%")
    @Max(value = 100, message = "Funds loss prevention percentage must be at most 100%")
    private Integer fundsLossPreventionPercentage;

    @Min(value = 10, message = "Initial time must be at least 10 seconds")
    private Integer initialTime;

    @Min(value = 10, message = "Reset time must be at least 10 seconds")
    private Integer resetTime;

    @Min(value = 500, message = "Maximum bid amount must be at least 1000")
    private Integer maximumBidAmount;

    @Min(value = 1, message = "Minimum bid increment must be at least 5")
    private Integer minimumBidIncrement;
}
