package com.example.payment_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private boolean isDown = false;

    @PostMapping("/toggleFailure")
    public ResponseEntity<String> toggleFailure() {
        isDown = !isDown;
        return ResponseEntity.ok("Payment service is now " + (isDown ? "DOWN" : "UP"));
    }

    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestParam String userId, @RequestParam String upiId) {
        if (isDown) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Payment service is down");
        }
        return ResponseEntity.ok("Payment successful for user " + userId);
    }
}
