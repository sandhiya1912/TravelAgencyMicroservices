package com.example.hotel_services.controller;

import com.example.hotel_services.model.Hotel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/hotels")
public class HotelController {
    private final List<Hotel> hotels = Arrays.asList(
            new Hotel("HotelA", "Paris", 100),
            new Hotel("HotelB", "London", 150)
    );

    @GetMapping("/available")
    public ResponseEntity<List<Hotel>> getHotels(@RequestParam String destination) {
        return ResponseEntity.ok(hotels.stream().filter(h -> h.getLocation().equals(destination)).toList());
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookHotel(@RequestParam String hotelId) {
        return ResponseEntity.ok("Hotel " + hotelId + " booked");
    }

    @PostMapping("/rollback")
    public ResponseEntity<String> rollbackHotel(@RequestParam String userId) {
        return ResponseEntity.ok("Hotel booking for user " + userId + " rolled back");
    }
}
