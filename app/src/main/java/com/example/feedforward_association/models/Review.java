package com.example.feedforward_association.models;

import com.example.feedforward_association.models.server.object.CreatedBy;
import com.example.feedforward_association.models.server.object.ObjectBoundary;
import com.example.feedforward_association.models.server.object.ObjectId;
import com.example.feedforward_association.models.server.user.UserSession;
import com.google.gson.Gson;

import java.util.Map;

public class Review {
    private String name;
    private String overview;
    private String date;
    private float rating;

    public Review() {
    }

    public Review(String name, String overview, String date, float rating) {
        this.name = name;
        this.overview = overview;
        this.date = date;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String toString() {
        return "Review{" +
                "name='" + name + '\'' +
                ", overview='" + overview + '\'' +
                ", date='" + date + '\'' + ", rating=" + rating + '}';
    }
    public ObjectBoundary converToObjectBoundary() {
        ObjectBoundary objectBoundary= new ObjectBoundary();
        objectBoundary.setType("Review");
        objectBoundary.setObjectId(new ObjectId(UserSession.getInstance().getSUPERAPP(),this.name));
        objectBoundary.setActive(true);
        objectBoundary.setCreatedBy(new CreatedBy(UserSession.getInstance().getSUPERAPP(),UserSession.getInstance().getUserEmail()));
        Gson gson = new Gson();
        Map<String, Object> orderMap = Map.of("Review",gson.toJson(this, Order.class));
        objectBoundary.setObjectDetails(orderMap);
        return objectBoundary;
    }
}
