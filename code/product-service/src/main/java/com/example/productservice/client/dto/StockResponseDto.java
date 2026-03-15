package com.example.productservice.client.dto;

public class StockResponseDto {
    private String productNumber;
    private int numberInStock;

    public StockResponseDto() {
    }

    public StockResponseDto(String productNumber, int numberInStock) {
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
