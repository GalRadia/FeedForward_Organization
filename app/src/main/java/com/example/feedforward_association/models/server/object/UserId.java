package com.example.feedforward_association.models.server.object;

public class UserId {
    private String superapp;
    private String email;

    public UserId(String superAppName, String email) {
        this.superapp = superAppName;
        this.email = email;
    }

    public UserId() {
    }

    public String getSuperapp() {
        return superapp;
    }

    public UserId setSuperapp(String superapp) {
        this.superapp = superapp;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserId setEmail(String email) {
        this.email = email;
        return this;
    }
}
