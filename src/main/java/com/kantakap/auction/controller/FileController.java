package com.kantakap.auction.controller;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    @PutMapping(value = "/auction/{auctionId}/players-data", consumes = "multipart/form-data")
//    @Secured("ROLE_USER")
    public List<List<String>> uploadPlayersData(@PathVariable String auctionId, @RequestPart MultipartFile file) {
        try {
            Reader reader = new InputStreamReader(file.getInputStream());
            List<List<String>> records = new ArrayList<>();
            try (CSVReader csvReader = new CSVReader(reader)) {
                String[] values = null;
                while ((values = csvReader.readNext()) != null) {
                    records.add(Arrays.asList(values));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return records;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
