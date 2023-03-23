package com.kantakap.auction.service;

import com.kantakap.auction.model.Bid;
import com.kantakap.auction.model.Bidder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public interface BidService {
    Mono<Bid> bid(Bidder bidder, String auctionId, Integer amount, LocalDateTime bidTime);
}
