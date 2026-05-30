package com.example.ridego.controller;

import com.example.ridego.dto.CreateRideRequest;
import com.example.ridego.model.Ride;
import com.example.ridego.service.RideService;

import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    private String getCurrentUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping("/rides")
    public Ride createRide(@Valid @RequestBody CreateRideRequest request) {
        return rideService.createRide(getCurrentUsername(), request);
    }

    @GetMapping("/user/rides")
    public List<Ride> getUserRides() {
        return rideService.getUserRides(getCurrentUsername());
    }

    @PostMapping("/rides/{rideId}/complete")
    public Ride completeRide(@PathVariable String rideId) {
        return rideService.completeRide(getCurrentUsername(), rideId);
    }
}