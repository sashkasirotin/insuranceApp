package com.insurance.insuranceApp.services.security.dto;

public class AuthenticationRequest {

    private String clientId ;
    private String contactMethodeType;
    private String contactMethodeValue;
    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String clientId, String password, String contactMethodeValue, String contactMethodeType) {
        this.clientId = clientId;
        this.password = password;
        this.contactMethodeValue = contactMethodeValue;
        this.contactMethodeType = contactMethodeType;
    }

    public String getContactMethodeValue() {
        return contactMethodeValue;
    }

    public void setContactMethodeValue(String contactMethodeValue) {
        this.contactMethodeValue = contactMethodeValue;
    }

    public String getContactMethodeType() {
        return contactMethodeType;
    }

    public void setContactMethodeType(String contactMethodeType) {
        this.contactMethodeType = contactMethodeType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return clientId;
    }

    public void setUsername(String username) {
        this.clientId = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
