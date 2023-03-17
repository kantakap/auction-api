package com.kantakap.auction.service;

import com.kantakap.auction.model.Auction;
import com.kantakap.auction.model.User;
import com.kantakap.auction.payload.CreateAuction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface AuctionService {

    Mono<Auction> createAuction(User creator, CreateAuction createAuction);
}
