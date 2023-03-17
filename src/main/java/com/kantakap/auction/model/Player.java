package com.kantakap.auction.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Player {
    private String id;
    private Long osuId;
    private String username;
    private String country;
    private String rank;
    private String qualifierRank;
}
