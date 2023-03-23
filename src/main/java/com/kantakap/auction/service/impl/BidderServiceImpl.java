package com.kantakap.auction.service.impl;

import com.kantakap.auction.model.Bidder;
import com.kantakap.auction.model.User;
import com.kantakap.auction.repository.BidderRepository;
import com.kantakap.auction.service.BidderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BidderServiceImpl implements BidderService {
    private final BidderRepository bidderRepository;

    /**
     * Get bidder from user
     * @param user user
     * @return bidder
     */
    @Override
    public Mono<Bidder> getBidderFromUser(User user) {
        return bidderRepository.findBidderByUserId(user.getId());
    }
}
