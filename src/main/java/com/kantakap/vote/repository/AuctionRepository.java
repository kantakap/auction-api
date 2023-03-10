package com.kantakap.vote.repository;

import com.kantakap.vote.model.Auction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends ReactiveMongoRepository<Auction, String> {
}
