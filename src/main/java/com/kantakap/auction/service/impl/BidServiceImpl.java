package com.kantakap.auction.service.impl;

import com.kantakap.auction.model.Bid;
import com.kantakap.auction.model.Bidder;
import com.kantakap.auction.repository.AuctionRepository;
import com.kantakap.auction.repository.BidRepository;
import com.kantakap.auction.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;

    @Override
    public Mono<Bid> bid(Bidder bidder, String auctionId, Integer amount, LocalDateTime bidTime) {
        return Mono.empty();
    }
}
