package com.example.stockservice.controller;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    Logger logger = LoggerFactory.getLogger(StockController.class);
    
    @GetMapping("/{productNumber}")
    public StockResponse getStock(@PathVariable String productNumber) {
        // Hard-coded stock values
        int stock = switch (productNumber) {
            case "PROD001" -> 100;
            case "PROD002" -> 50;
            case "PROD003" -> 25;
            case "PROD004" -> 75;
            case "PROD005" -> 150;
            default -> 0;
        };
        logger.info("Returning stock for product {}: {}", productNumber, stock);
        return new StockResponse(productNumber, stock);
    }

    public static class StockResponse {
        private String productNumber;
        private int numberInStock;

        public StockResponse(String productNumber, int numberInStock) {
            this.productNumber = productNumber;
            this.numberInStock = numberInStock;
        }

        public String getProductNumber() {
            return productNumber;
        }

        public void setProductNumber(String productNumber) {
            this.productNumber = productNumber;
        }

        public int getNumberInStock() {
            return numberInStock;
        }

        public void setNumberInStock(int numberInStock) {
            this.numberInStock = numberInStock;
        }
    }

}
