package com.example.hotel_services.model;

public class Hotel {
    private String id;
    private String location;
    private double price;

    public Hotel(String id, String location, double price) {
        this.id = id;
        this.location = location;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
