package com.kantakap.auction.service.impl;

import com.kantakap.auction.service.FileProcessorService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileProcessorServiceImpl implements FileProcessorService {

    @Override
    public Flux<List<String>> readCsvData(FilePart filePart) {
        Flux<DataBuffer> dataBufferFlux = filePart.content();
        return DataBufferUtils.join(dataBufferFlux)
                .map(dataBuffer -> dataBuffer.asInputStream(true))
                .map(inputStream -> new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .map(CSVReader::new)
                .flatMapIterable(csvReader -> {
                    try {
                        return csvReader.readAll();
                    } catch (IOException | CsvException e) {
                        throw new RuntimeException("Error occurred while reading CSV file");
                    }
                })
                .map(Arrays::asList);
    }
}
