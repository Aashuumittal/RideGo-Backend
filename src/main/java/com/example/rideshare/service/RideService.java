package com.example.ridego.service;

import com.example.ridego.dto.CreateRideRequest;
import com.example.ridego.model.Ride;

import java.util.List;

public interface RideService {

    Ride createRide(String username, CreateRideRequest request);

    List<Ride> getUserRides(String username);

    List<Ride> getPendingRides();

    Ride acceptRide(String username, String rideId);

    Ride completeRide(String username, String rideId);
}