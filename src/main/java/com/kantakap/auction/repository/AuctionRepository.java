package com.kantakap.auction.repository;

import com.kantakap.auction.model.Auction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends ReactiveMongoRepository<Auction, String> {
}
