package com.example.feedforward_association.models;

import com.example.feedforward_association.models.server.object.CreatedBy;
import com.example.feedforward_association.models.server.object.Location;
import com.example.feedforward_association.models.server.object.ObjectBoundary;
import com.example.feedforward_association.models.server.object.ObjectId;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Restaurant {
    private ObjectId restaurantId;
    private String restaurantEmail;
    private String restaurantName;
    private String restaurantAddress;
    private Location restaurantLocation;
    private String restaurantPhone;
    private List<Food> storage;
   // private List<Order> orders;
    private List<Review>reviews;

    public Restaurant(ObjectId restaurantId, String restaurantEmail, String restaurantName, String restaurantAddress, Location restaurantLocation, String restaurantPhone, List<Food> storage, List<Review> reviews) {
        this.restaurantId = restaurantId;
        this.restaurantEmail = restaurantEmail;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantLocation = restaurantLocation;
        this.restaurantPhone = restaurantPhone;
        this.storage = storage;
      //  this.orders = orders;
        this.reviews = reviews;
    }
    public Restaurant() {
    }
    public Restaurant(ObjectBoundary objectBoundary) {
        Gson gson = new Gson();
        Restaurant temp =gson.fromJson((String) objectBoundary.getObjectDetails().get("Restaurant"), Restaurant.class);
        this.restaurantId = objectBoundary.getObjectId();
        this.restaurantEmail = temp.getRestaurantEmail();
        this.restaurantName = temp.getRestaurantName();
        this.restaurantAddress = temp.getRestaurantAddress();
        this.restaurantLocation = temp.getRestaurantLocation();
        this.restaurantPhone = temp.getRestaurantPhone();
        this.storage = temp.getStorage();
       // this.orders = temp.getOrders();
        this.reviews = temp.getReviews();
    }

    public ObjectId getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(ObjectId restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantEmail() {
        return restaurantEmail;
    }

    public void setRestaurantEmail(String restaurantEmail) {
        this.restaurantEmail = restaurantEmail;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public Location getRestaurantLocation() {
        return restaurantLocation;
    }

    public void setRestaurantLocation(Location restaurantLocation) {
        this.restaurantLocation = restaurantLocation;
    }

    public String getRestaurantPhone() {
        return restaurantPhone;
    }

    public void setRestaurantPhone(String restaurantPhone) {
        this.restaurantPhone = restaurantPhone;
    }

    public List<Food> getStorage() {
        return storage;
    }

    public void setStorage(List<Food> storage) {
        this.storage = storage;
    }

//    public List<Order> getOrders() {
//        return orders;
//    }
//
//    public void setOrders(List<Order> orders) {
//        this.orders = orders;
//    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "restaurantId='" + restaurantId + '\'' +
                ", restaurantEmail='" + restaurantEmail + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurantAddress='" + restaurantAddress + '\'' +
                ", restaurantLocation=" + restaurantLocation +
                ", restaurantPhone='" + restaurantPhone + '\'' +
                ", storage=" + storage +
//                ", orders=" + orders +
                ", reviews=" + reviews +
                '}';
    }
    public static List<Restaurant> convertObjectBoundaryList(List<ObjectBoundary> objectBoundaries) {
        List<Restaurant> restaurants = new ArrayList<>();
        for (ObjectBoundary objectBoundary : objectBoundaries) {
            restaurants.add(new Restaurant(objectBoundary));
        }
        return restaurants;
    }
    public ObjectBoundary convert(Restaurant restaurant) {
        ObjectBoundary objectBoundary = new ObjectBoundary();
        objectBoundary.setObjectId(restaurantId);
        objectBoundary.setType("Order");
        objectBoundary.setAlias(restaurantEmail);
        objectBoundary.setCreatedBy(new CreatedBy("2024b.gal.said", "ziv@gmail.com"));
        objectBoundary.setLocation(new Location(100.0, 100.0));//TODO: get location from device
        objectBoundary.setActive(true);
        Gson gson = new Gson();
        Map<String, Object> orderMap = Map.of("order",gson.toJson(restaurant, Restaurant.class));
        objectBoundary.setObjectDetails(orderMap);
        return objectBoundary;
    }
}
