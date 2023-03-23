package com.kantakap.auction.model;

import lombok.Data;

@Data
public class Bidder {
    private User user;
    private Integer balance;
}
