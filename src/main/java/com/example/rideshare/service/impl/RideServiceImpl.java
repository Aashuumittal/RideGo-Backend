package com.example.ridego.service.impl;

import com.example.ridego.dto.CreateRideRequest;
import com.example.ridego.exception.BadRequestException;
import com.example.ridego.exception.NotFoundException;
import com.example.ridego.model.Ride;
import com.example.ridego.model.User;
import com.example.ridego.repository.RideRepository;
import com.example.ridego.repository.UserRepository;
import com.example.ridego.service.RideService;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepo;
    private final UserRepository userRepo;

    public RideServiceImpl(RideRepository rideRepo, UserRepository userRepo) {
        this.rideRepo = rideRepo;
        this.userRepo = userRepo;
    }

    private User getUser(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public Ride createRide(String username, CreateRideRequest request) {
        User user = getUser(username);

        if (!user.getRole().equals("ROLE_USER")) {
            throw new BadRequestException("Only users can request rides");
        }

        Ride ride = Ride.builder()
                .userId(user.getId())
                .pickupLocation(request.getPickupLocation())
                .dropLocation(request.getDropLocation())
                .status("REQUESTED")
                .createdAt(new Date())
                .build();

        return rideRepo.save(ride);
    }

    @Override
    public List<Ride> getUserRides(String username) {
        User user = getUser(username);
        return rideRepo.findByUserId(user.getId());
    }

    @Override
    public List<Ride> getPendingRides() {
        return rideRepo.findByStatus("REQUESTED");
    }

    @Override
    public Ride acceptRide(String username, String rideId) {
        User driver = getUser(username);

        if (!driver.getRole().equals("ROLE_DRIVER")) {
            throw new BadRequestException("Only drivers can accept rides");
        }

        Ride ride = rideRepo.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!ride.getStatus().equals("REQUESTED")) {
            throw new BadRequestException("Ride is not available");
        }

        ride.setDriverId(driver.getId());
        ride.setStatus("ACCEPTED");

        return rideRepo.save(ride);
    }

    @Override
    public Ride completeRide(String username, String rideId) {
        User user = getUser(username);

        Ride ride = rideRepo.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!ride.getStatus().equals("ACCEPTED")) {
            throw new BadRequestException("Ride cannot be completed");
        }

        if (!ride.getDriverId().equals(user.getId())
                && !ride.getUserId().equals(user.getId())) {
            throw new BadRequestException("Only the driver or passenger can complete this ride");
        }

        ride.setStatus("COMPLETED");

        return rideRepo.save(ride);
    }
}