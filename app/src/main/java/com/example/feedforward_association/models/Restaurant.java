package com.example.feedforward_association.models;

import com.example.feedforward_association.models.server.object.CreatedBy;
import com.example.feedforward_association.models.server.object.Location;
import com.example.feedforward_association.models.server.object.ObjectBoundary;
import com.example.feedforward_association.models.server.object.ObjectId;
import com.example.feedforward_association.models.server.user.UserSession;
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

    public Restaurant(ObjectId restaurantId, String restaurantEmail, String restaurantName, String restaurantAddress, Location restaurantLocation, String restaurantPhone, List<Food> storage) {
        this.restaurantId = restaurantId;
        this.restaurantEmail = restaurantEmail;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantLocation = restaurantLocation;
        this.restaurantPhone = restaurantPhone;
        this.storage = storage;
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

    }

    public ObjectId getRestaurantId() {
        return restaurantId;
    }

    public Restaurant setRestaurantId(ObjectId restaurantId) {
        this.restaurantId = restaurantId;
        return this;
    }

    public String getRestaurantEmail() {
        return restaurantEmail;
    }

    public Restaurant setRestaurantEmail(String restaurantEmail) {
        this.restaurantEmail = restaurantEmail;
        return this;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public Restaurant setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
        return this;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public Restaurant setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
        return this;
    }

    public Location getRestaurantLocation() {
        return restaurantLocation;
    }

    public Restaurant setRestaurantLocation(Location restaurantLocation) {
        this.restaurantLocation = restaurantLocation;
        return this;
    }

    public String getRestaurantPhone() {
        return restaurantPhone;
    }

    public Restaurant setRestaurantPhone(String restaurantPhone) {
        this.restaurantPhone = restaurantPhone;
        return this;
    }

    public List<Food> getStorage() {
        return storage;
    }

    public Restaurant setStorage(List<Food> storage) {
        this.storage = storage;
        return this;
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
                '}';
    }
    public static List<Restaurant> convertObjectBoundaryList(List<ObjectBoundary> objectBoundaries) {
        List<Restaurant> restaurants = new ArrayList<>();
        for (ObjectBoundary objectBoundary : objectBoundaries) {
            restaurants.add(new Restaurant(objectBoundary));
        }
        return restaurants;
    }
    public ObjectBoundary toObjectBoundary() {
        ObjectBoundary objectBoundary = new ObjectBoundary();
        objectBoundary.setObjectId(restaurantId);
        objectBoundary.setType("Restaurant");
        objectBoundary.setAlias(restaurantEmail);
        objectBoundary.setCreatedBy(new CreatedBy(UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getUserEmail()));
        objectBoundary.setLocation(restaurantLocation);//TODO: get location from device
        objectBoundary.setActive(true);
        Gson gson = new Gson();
        Map<String, Object> orderMap = Map.of("Restaurant",gson.toJson(this, Restaurant.class));
        objectBoundary.setObjectDetails(orderMap);
        return objectBoundary;
    }
}
