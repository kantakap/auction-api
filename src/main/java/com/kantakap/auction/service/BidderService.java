package com.kantakap.auction.service;

import com.kantakap.auction.model.Bidder;
import com.kantakap.auction.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface BidderService {
    Mono<Bidder> getBidderFromUser(User user);
}
