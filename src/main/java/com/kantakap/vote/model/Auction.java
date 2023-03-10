package com.kantakap.vote.model;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Builder
public class Auction {
    private String id;
    private User createdBy;
    private String title;
    private LocalDateTime startsAt;
    private List<Player> players;
}
