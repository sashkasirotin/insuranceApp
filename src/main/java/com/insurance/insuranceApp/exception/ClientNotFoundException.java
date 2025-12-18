package com.insurance.insuranceApp.exception;

public class ClientNotFoundException extends BusinessException {
    public ClientNotFoundException(String clientId) {
        super("Client not found: " + clientId);
    }
}

