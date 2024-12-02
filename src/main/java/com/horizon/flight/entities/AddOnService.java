package com.horizon.flight.entities;

public class AddOnService {
    private String name;
    private double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }


    public String getName() {
        return name;
    }


    public double getPrice() {
        return price;
    }


    public String getDescription() {
        return "Add-on: " + name + ", Price: " + price;
    }
}