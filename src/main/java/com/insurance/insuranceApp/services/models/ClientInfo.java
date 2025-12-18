package com.insurance.insuranceApp.services.models;


//Entity
//@Table(name = "clients")
public class ClientInfo {
    private String id;
    private String username;
    private String passwordHash;
    private String role;
    private String contactMethodeType;
    private String contactMethodeValue;

    public ClientInfo(String id, String contactMethodeValue, String contactMethodeType, String role, String passwordHash, String username) {
        this.id = id;
        this.contactMethodeValue = contactMethodeValue;
        this.contactMethodeType = contactMethodeType;
        this.role = role;
        this.passwordHash = passwordHash;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public ClientInfo() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return passwordHash;
    }

    public void setPassword(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}

