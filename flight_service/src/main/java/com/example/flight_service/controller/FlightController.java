package com.example.flight_service.controller;

import com.example.flight_service.model.Flight;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {
    private final List<Flight> flights = Arrays.asList(
            new Flight("Flight123", "CityA", "Paris", 300),
            new Flight("Flight456", "CityA", "London", 250)
    );

    @GetMapping("/available")
    public ResponseEntity<List<Flight>> getFlights(@RequestParam String destination) {
        return ResponseEntity.ok(flights.stream().filter(f -> f.getDestination().equals(destination)).toList());
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookFlight(@RequestParam String flightId) {
//        if (Math.random() >0) { // Simulating failure (30% chance)
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Flight booking failed!");
//        }
        return ResponseEntity.ok("Flight " + flightId + " booked");
    }

    @PostMapping("/rollback")
    public ResponseEntity<String> rollbackFlight(@RequestParam String userId) {
        return ResponseEntity.ok("Flight booking for user " + userId + " rolled back");
    }
}
