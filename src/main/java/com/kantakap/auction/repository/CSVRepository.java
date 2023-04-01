package com.kantakap.auction.repository;

import com.kantakap.auction.model.CSV;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CSVRepository extends ReactiveMongoRepository<CSV, String> {
    Mono<CSV> findByAuctionId(String auctionId);
    Mono<Void> deleteAllByAuctionId(String auctionId);
}
