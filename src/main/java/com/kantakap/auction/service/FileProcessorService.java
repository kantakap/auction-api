package com.kantakap.auction.service;

import com.kantakap.auction.model.CSV;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;

@Service
public interface FileProcessorService {
    File binaryToFile(Binary binary, String fileName);
    Mono<CSV> findCSVByAuctionId(String auctionId);
    Mono<Void> deleteCSVByAuctionId(String auctionId);
}
