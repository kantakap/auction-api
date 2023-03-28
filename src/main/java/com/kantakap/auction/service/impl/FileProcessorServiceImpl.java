package com.kantakap.auction.service.impl;

import com.kantakap.auction.model.CSV;
import com.kantakap.auction.repository.CSVRepository;
import com.kantakap.auction.service.FileProcessorService;
import lombok.RequiredArgsConstructor;
import org.bson.types.Binary;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Repository
@RequiredArgsConstructor
public class FileProcessorServiceImpl implements FileProcessorService {
    private final CSVRepository csvRepository;
    @Override
    public File binaryToFile(Binary binary, String fileName) {
        File file = new File(fileName);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(binary.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    @Override
    public Mono<CSV> findCSVByAuctionId(String auctionId) {
        return csvRepository.findByAuctionId(auctionId)
                .switchIfEmpty(Mono.error(new RuntimeException("CSV not found.")));
    }

    @Override
    public Mono<Void> deleteCSVByAuctionId(String auctionId) {
        return csvRepository.deleteByAuctionId(auctionId);
    }
}
