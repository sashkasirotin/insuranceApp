package com.insurance.insuranceApp.exception;

public class ProductNotFoundException
        extends BusinessException {
    public ProductNotFoundException(String productId) {
        super("Product not found id: " + productId);
    }
}

