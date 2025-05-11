package com.example.booking_service.controller;

import com.example.booking_service.model.Booking;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController
@RequestMapping("/booking")
public class BookingController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String FLIGHT_SERVICE_URL = "http://localhost:8081/flights";
    private final String HOTEL_SERVICE_URL = "http://localhost:8082/hotels";
    private final String PAYMENT_SERVICE_URL = "http://localhost:8083/payment";
    private final Map<String, Booking> bookings = new HashMap<>();

    @PostMapping("/create")
    public ResponseEntity<String> createBooking(@RequestParam String userId, @RequestParam String destination) {
        bookings.put(userId, new Booking(userId, destination));
        return ResponseEntity.ok("Booking created for user " + userId + " to " + destination);
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookTrip(@RequestParam String userId,
                                           @RequestParam String flightId,
                                           @RequestParam String hotelId,
                                           @RequestParam String upiId) {
        Booking booking = bookings.get(userId);
        if (booking == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found for user " + userId);
        }

        try {
            // Step 1: Book Flight
            ResponseEntity<String> flightResponse = restTemplate.postForEntity(
                    FLIGHT_SERVICE_URL + "/book?flightId=" + flightId, null, String.class);
            booking.addBookedFlight(flightId);

            // Step 2: Book Hotel
            ResponseEntity<String> hotelResponse = restTemplate.postForEntity(
                    HOTEL_SERVICE_URL + "/book?hotelId=" + hotelId, null, String.class);
            booking.addBookedHotel(hotelId);

            // Step 3: Process Payment
            ResponseEntity<String> paymentResponse = restTemplate.postForEntity(
                    PAYMENT_SERVICE_URL + "/process?userId=" + userId + "&upiId=" + upiId, null, String.class);

            System.out.println("UserId: " + userId);
            System.out.println("FlightId: " + flightId);
            System.out.println("HotelId: " + hotelId);
            System.out.println("UPI Id: " + upiId);

            return ResponseEntity.ok("Booking confirmed for user " + userId);

        } catch (HttpServerErrorException e) {
            // If payment service fails (HTTP 503), rollback and show correct error message
            if (e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                System.out.println("Payment service is down. Triggering rollback.");
                rollbackBooking(userId);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Booking failed due to payment service being down, rollback triggered");
            } else {
                System.out.println("Unexpected error occurred: " + e.getMessage());
                rollbackBooking(userId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Booking failed due to an unknown error, rollback triggered");
            }
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            rollbackBooking(userId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Booking failed due to an unknown error, rollback triggered");
        }
    }


    private void rollbackBooking(String userId) {
        Booking booking = bookings.get(userId);
        if (booking == null) return;

        Set<String> processedFlights = new HashSet<>();
        Set<String> processedHotels = new HashSet<>();

        for (String flightId : booking.getBookedFlights()) {
            if (!processedFlights.contains(flightId)) {
                restTemplate.postForObject(FLIGHT_SERVICE_URL + "/rollback?userId=" + userId, null, String.class);
                System.out.println("Your booked flight ticket " + flightId + " is cancelled due to payment failure.");
                processedFlights.add(flightId);
            }
        }

        for (String hotelId : booking.getBookedHotels()) {
            if (!processedHotels.contains(hotelId)) {
                restTemplate.postForObject(HOTEL_SERVICE_URL + "/rollback?userId=" + userId, null, String.class);
                System.out.println("Your hotel reservation " + hotelId + " is cancelled due to payment failure.");
                processedHotels.add(hotelId);
            }
        }
    }

}
