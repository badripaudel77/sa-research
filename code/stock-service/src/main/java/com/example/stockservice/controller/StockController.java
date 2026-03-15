package com.example.stockservice.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    /**
     * Returns the number of products in stock for a given product number
     */
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
