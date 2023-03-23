package com.kantakap.auction.repository;

import com.kantakap.auction.model.Bid;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BidRepository extends ReactiveMongoRepository<Bid, String> {
    Mono<Bid> findBidByAuctionIdAndIsOngoingTrue(String auctionId);
}
