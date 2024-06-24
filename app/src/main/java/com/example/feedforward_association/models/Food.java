package com.example.feedforward_association.models;

import java.util.OptionalInt;
import java.util.UUID;

public class Food {
    private String id;
    private String name;
    private String description;
    private int quantity;
    private OptionalInt currentQuantity;

    public Food(){

    }

    public Food(String name, String description, int quantity) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.currentQuantity = OptionalInt.empty();
    }
    public String getId() {
        return id;
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

    public int getQuantity() {
        return quantity;
    }

    public Food setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
    public OptionalInt getCurrentQuantity() {
        return currentQuantity;
    }
    public Food setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = OptionalInt.of(currentQuantity);
        return this;
    }
    @Override
    public String toString() {
        return name + " - " + description + " - " + quantity;
    }
}
