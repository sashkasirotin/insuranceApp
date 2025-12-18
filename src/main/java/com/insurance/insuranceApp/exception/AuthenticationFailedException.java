package com.insurance.insuranceApp.exception;

public class AuthenticationFailedException
        extends BusinessException {
    public AuthenticationFailedException() {
        super("Invalid credentials");
    }
}

