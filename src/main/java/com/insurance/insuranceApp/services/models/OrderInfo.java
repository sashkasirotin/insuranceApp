package com.insurance.insuranceApp.services.models;

import java.time.LocalDateTime;
import java.util.*;

public class OrderInfo {
    private int orderId;
    private String clientId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Set<String> products;

    public OrderInfo(Set <String> products) {
        this.products = products;
    }

    public OrderInfo() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public OrderInfo(Set<String> products, LocalDateTime endDate, LocalDateTime startDate, String clientId, int orderId) {
        this.products = products;
        this.endDate = endDate;
        this.startDate = startDate;
        this.clientId = clientId;
        this.orderId = orderId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Set<String> getProducts() {
        return products;
    }

}
