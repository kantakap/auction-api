package com.kantakap.vote.service.impl;

import com.kantakap.vote.model.Auction;
import com.kantakap.vote.payload.CreateAuction;
import com.kantakap.vote.repository.AuctionRepository;
import com.kantakap.vote.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;

    @Override
    public Mono<Auction> createAuction(CreateAuction createAuction) {
        return auctionRepository.save(Auction.builder().title(createAuction.getTitle()).build())
                .log();
    }
}
