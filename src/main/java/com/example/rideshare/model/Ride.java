package com.example.ridego.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "rides")
public class Ride {

    @Id
    private String id;

    private String userId;
    private String driverId;

    private String pickupLocation;
    private String dropLocation;

    private String status; // REQUESTED / ACCEPTED / COMPLETED
    private Date createdAt;
}