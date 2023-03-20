package com.kantakap.auction.service;

import com.kantakap.auction.model.Auction;
import com.kantakap.auction.model.User;
import com.kantakap.auction.payload.CreateAuction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface AuctionService {

    Mono<Auction> findAuctionById(String auctionId);
    Mono<Auction> createAuction(User creator, CreateAuction createAuction);

    Mono<Auction> startAuction(User user, String auctionId);
}
