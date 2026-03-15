package com.example.productservice.client;

import com.example.productservice.client.dto.StockResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "stock-service", url = "${stock.service.url}")
public interface StockServiceClient {

    @GetMapping("/api/stock/{productNumber}")
    StockResponseDto getStock(@PathVariable String productNumber);

}
