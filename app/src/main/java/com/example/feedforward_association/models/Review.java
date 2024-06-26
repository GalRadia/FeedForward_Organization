package com.example.feedforward_association.models;

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
}
