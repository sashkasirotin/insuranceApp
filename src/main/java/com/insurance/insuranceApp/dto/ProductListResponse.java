package com.insurance.insuranceApp.dto;

import com.insurance.insuranceApp.services.models.ProductInfo;

import java.util.List;
import java.util.Map;

public class ProductListResponse {

    public Map<String, ProductInfo> getProducts() {
        return products;
    }

    public void setProducts(Map<String, ProductInfo> products) {
        this.products = products;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    private String customerId;
    private Map<String, ProductInfo> products;
    public ProductListResponse(){}
}
