package com.insurance.insuranceApp.services.models;

import java.time.LocalDateTime;
import java.util.Date;

public class ProductInfo {

    private String productId;
    private String name;
    private String description;
    private LocalDateTime effectiveDate;
    private LocalDateTime expirationDate;
    private double price;

    public ProductInfo(String productId, String name, String description, LocalDateTime effectiveDate, LocalDateTime expirationDate, double price) {
        this.productId = productId;
        this.name=name;
        this.description = description;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.price = price;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public ProductInfo() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
