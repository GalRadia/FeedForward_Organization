package com.example.feedforward_association.models.server.user;

import com.example.feedforward_association.models.Association;

public class UserSession {
    private static UserSession instance;
    private String boundaryId;
    private String userEmail;
    private final String SUPERAPP= "2024b.gal.said";
    private Association association;


    public Association getAssociation() {
        return association;
    }

    public UserSession setAssociation(Association association) {
        this.association = association;
        return this;
    }

    public String getSUPERAPP() {
        return SUPERAPP;
    }

    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setBoundaryId(String boundaryId) {
        this.boundaryId = boundaryId;
    }

    public String getBoundaryId() {
        return boundaryId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void clearSession() {
        this.boundaryId = null;
        this.userEmail = null;
    }
}
