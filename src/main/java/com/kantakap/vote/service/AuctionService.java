package com.kantakap.vote.service;

import com.kantakap.vote.model.Auction;
import com.kantakap.vote.payload.CreateAuction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface AuctionService {

    Mono<Auction> createAuction(CreateAuction createAuction);
}
