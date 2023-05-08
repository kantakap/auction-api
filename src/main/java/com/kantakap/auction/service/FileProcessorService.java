package com.kantakap.auction.service;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public interface FileProcessorService {
    Flux<List<String>> readCsvData(FilePart filePart);
}
