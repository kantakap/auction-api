package com.kantakap.auction.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Builder
@Getter
@Setter
public class Auction {
    @Id
    @Setter(lombok.AccessLevel.NONE)
    private String id;

    // who created the auction
    @Setter(lombok.AccessLevel.NONE)
    private User createdBy;

    // who can manage the auction
    private List<User> staff;

    // title of the auction
    private String title;

    // when the auction can be started
    private LocalDateTime startsAt;

    // list of players to be auctioned
    private List<Player> players;

    // list of bidders / captains
    private List<User> bidders;

    // AUCTION PROCESS ========================================
    // current status of the auction
    private AuctionStatus status;

    // initial bidders balance
    private Integer initialBalance;

    // funds loss prevention percentage
    private Integer fundsLossPreventionPercentage;

    // initial time for the auction
    private Integer initialTime;

    // reset time for the auction (after a bid)
    private Integer resetTime;

    // maximum bid amount
    private Integer maximumBidAmount;

    // minimum bid increment
    private Integer minimumBidIncrement;

    // DISCORD RELATED ========================================
    // guild id of the discord server
    private String guildId;

    // channel id of the discord channel
    private String channelId;

    // bidder role id of the discord server
    private String bidderRoleId;
}
