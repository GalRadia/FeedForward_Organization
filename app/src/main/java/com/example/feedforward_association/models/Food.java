package com.example.feedforward_association.models;

import java.util.OptionalInt;
import java.util.UUID;

public class Food {
    private String name;
    private String description;
    private int quantity;

    public Food(){

    }

    public Food(String name, String description, int quantity) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public Food setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Food setDescription(String description) {
        this.description = description;
        return this;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }



    @Override
    public String toString() {
        return name + " - " + description + " - " + quantity;
    }
}
