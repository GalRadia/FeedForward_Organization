package com.example.feedforward_association.models.server.object;

public class CreatedBy {
    private UserId userId;

    public CreatedBy() {
    }

    public CreatedBy(String superAppName, String email) {
        this.userId = new UserId(superAppName, email);
    }

    public UserId getUserId() {
        return userId;
    }

    public CreatedBy setUserId(UserId userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public String toString() {
        return "CreatedBy [userId=" + userId + "]";
    }


}
