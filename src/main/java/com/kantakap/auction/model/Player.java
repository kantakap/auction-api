package com.kantakap.auction.model;

import lombok.Data;

@Data
public class Player {
    private Long osuId;
    private String username;
    private String country;
    private String rank;
    private String qualifierRank;
}
