package com.kantakap.auction.controller;

import com.kantakap.auction.service.FileProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {
    private final FileProcessorService fileProcessorService;

    @PostMapping(value = "/auction/{auctionId}/players-data", consumes = "multipart/form-data")
//    @Secured("ROLE_USER")
    public Flux<List<String>> uploadPlayersData(@PathVariable String auctionId, @RequestPart("file") FilePart file) {
        return fileProcessorService.readCsvData(file);
    }
}
