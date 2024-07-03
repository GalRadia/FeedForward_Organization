package com.example.feedforward_association.models;

import com.example.feedforward_association.models.server.CommandOptions;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.UUID;

public class Food {
    private String name;
    private String type;
    private int amount;
    private String expiryDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return Objects.equals(name, food.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public Food(){

    }

    public Food(String name, String type, int amount, String expiryDate) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.expiryDate = expiryDate;
    }

    public String getType() {
        return type;
    }

    public Food setType(String type) {
        this.type = type;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public Food setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public Food setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public String getName() {
        return name;
    }

    public Food setName(String name) {
        this.name = name;
        return this;
    }








}
