package com.kantakap.auction.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Data
public class Bid {
    @MongoId
    private String id;
    private String auctionId;
    private Player player;
    private Bidder bidder;
    private Integer amount;

}

