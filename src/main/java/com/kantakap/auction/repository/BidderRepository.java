package com.kantakap.auction.repository;

import com.kantakap.auction.model.Bidder;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BidderRepository extends ReactiveMongoRepository<Bidder, String> {
    Mono<Bidder> findBidderByUserId(String userId);
}
