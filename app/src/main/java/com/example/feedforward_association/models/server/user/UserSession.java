package com.example.feedforward_association.models.server.user;

import com.example.feedforward_association.models.Association;

public class UserSession {
    private static UserSession instance;
    private UserBoundary user;
    private final  String SUPERAPP = "2024b.gal.said";
    private Association association;


    public Association getAssociation() {
        return association;
    }

    public UserSession setAssociation(Association association) {
        this.association = association;
        return this;
    }


    public UserBoundary getUser() {
        return user;
    }

    public UserSession setUser(UserBoundary user) {
        this.user = user;
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




}
