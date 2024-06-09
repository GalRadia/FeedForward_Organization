package com.example.feedforward_association.models;

import java.util.List;
import java.util.UUID;

public class Order {
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
}
