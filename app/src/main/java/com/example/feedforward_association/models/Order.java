package com.example.feedforward_association.models;

import com.example.feedforward_association.models.server.object.CreatedBy;
import com.example.feedforward_association.models.server.object.Location;
import com.example.feedforward_association.models.server.object.ObjectBoundary;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Converter;

public class Order implements Converter<Order, ObjectBoundary> {
    String orderID;
    String donatorName;
    String donatorLocation;
    String orderDate;
    String orderTime;
    List<Food> foods;
    OrderStatus orderStatus;

    public Order() {
    }

    public Order(String donatorName, String donatorLocation, String orderDate, String orderTime, List<Food> foods, OrderStatus orderStatus) {
        this.orderID = UUID.randomUUID().toString();
        this.donatorName = donatorName;
        this.donatorLocation = donatorLocation;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.foods = foods;
        this.orderStatus = orderStatus;
    }

    public Order(ObjectBoundary objectBoundary) {
        Order temp = (Order) objectBoundary.getObjectDetails().get("order");
        this.orderID = temp.getOrderID();
        this.donatorName = temp.getDonatorName();
        this.donatorLocation = temp.getDonatorLocation();
        this.orderDate = temp.getOrderDate();
        this.orderTime = temp.getOrderTime();
        this.foods = temp.getFoods();
        this.orderStatus = temp.getOrderStatus();
    }

    public String getOrderID() {
        return orderID;
    }

    public String getDonatorName() {
        return donatorName;
    }

    public Order setDonatorName(String donatorName) {
        this.donatorName = donatorName;
        return this;
    }

    public String getDonatorLocation() {
        return donatorLocation;
    }

    public Order setDonatorLocation(String donatorLocation) {
        this.donatorLocation = donatorLocation;
        return this;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public Order setOrderDate(String orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public Order setOrderTime(String orderTime) {
        this.orderTime = orderTime;
        return this;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public Order setFoods(List<Food> foods) {
        this.foods = foods;
        return this;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Order setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID='" + orderID + '\'' +
                ", donatorName='" + donatorName + '\'' +
                ", donatorLocation='" + donatorLocation + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", foods=" + foods +
                ", orderStatus=" + orderStatus +
                '}';
    }

    @Override
    public ObjectBoundary convert(Order order) throws IOException {
        ObjectBoundary objectBoundary = new ObjectBoundary();
        objectBoundary.setType("Order");
        objectBoundary.setAlias(order.getOrderID());
        objectBoundary.setCreatedBy(new CreatedBy("2024b.gal.said", "ziv@gmail.com"));
        objectBoundary.setLocation(new Location(100.0, 100.0));//TODO: get location from device
        objectBoundary.setActive(true);
        Map<String, Object> orderMap = Map.of("order", order);
        objectBoundary.setObjectDetails(orderMap);
        return objectBoundary;
    }
}
