package com.example.productservice.controller;

import com.example.productservice.client.StockServiceClient;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private StockServiceClient stockServiceClient;

    @Autowired(required = false)
    private ProductRepository productRepository;

    /**
     * Returns a Product given a product number.
     * Product details are loaded from the 'products' table in PostgreSQL.
     * Stock information is fetched from stock-service via Feign.
     * The @CircuitBreaker opens after repeated stock-service failures and
     * routes to getProductFallback instead.
     */
    // private endpoint , any role can access
    @GetMapping("/{productNumber}")
    @CircuitBreaker(name = "stockService", fallbackMethod = "getProductFallback")
    public Product getProduct(@PathVariable String productNumber) {
        Product product = findProductForResponse(productNumber);

        var stockResponse = stockServiceClient.getStock(productNumber);
        product.setNumberOnStock(stockResponse.getNumberInStock());
        return product;
    }

    public Product getProductFallback(String productNumber, Throwable throwable) {
        Product product = findProductOrDefault(productNumber);

        product.setNumberOnStock(0);
        product.setMessage("Stock service is currently unavailable. Fallback response returned.");
        return product;
    }

    private Product findProductForResponse(String productNumber) {
        if (productRepository == null) {
            Product demoProduct = new Product(productNumber, "Demo Product", 0);
            demoProduct.setMessage("Database is disabled. Returning demo product data.");
            return demoProduct;
        }

        return productRepository.findById(productNumber)
                .orElse(new Product(productNumber, "Unknown Product", 0));
    }

    private Product findProductOrDefault(String productNumber) {
        if (productRepository == null) {
            return new Product(productNumber, "Unknown Product", 0);
        }
        return productRepository.findById(productNumber)
                .orElse(new Product(productNumber, "Unknown Product", 0));
    }

    // welcome endpoint for testing
    // public endpoint
    @GetMapping("")
    public String welcome() {
        return "Welcome to Product Service";
    }

}
