package com.kantakap.vote.payload;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @Min(value = 10, message = "Initial time must be at least 10 seconds")
    private Integer initialTime;

    @Min(value = 10, message = "Reset time must be at least 10 seconds")
    private Integer resetTime;

    @Min(value = 1000, message = "Maximum bid amount must be at least 1000")
    private Integer maximumBidAmount;

    @Min(value = 1, message = "Minimum bid increment must be at least 5")
    private Integer minimumBidIncrement;
}
