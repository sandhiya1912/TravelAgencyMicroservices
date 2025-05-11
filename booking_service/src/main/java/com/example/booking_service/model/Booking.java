package com.example.booking_service.model;

import java.util.ArrayList;
import java.util.List;

public class Booking {
    private String userId;
    private String destination;
    private List<String> bookedFlights;
    private List<String> bookedHotels;

    public Booking(String userId, String destination) {
        this.userId = userId;
        this.destination = destination;
        this.bookedFlights = new ArrayList<>();
        this.bookedHotels = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<String> getBookedFlights() {
        return bookedFlights;
    }

    public void addBookedFlight(String flightId) {
        this.bookedFlights.add(flightId);
    }

    public List<String> getBookedHotels() {
        return bookedHotels;
    }

    public void addBookedHotel(String hotelId) {
        this.bookedHotels.add(hotelId);
    }
}
