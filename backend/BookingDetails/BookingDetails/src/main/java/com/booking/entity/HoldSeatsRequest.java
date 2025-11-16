package com.booking.entity;

import java.util.List;
import java.util.UUID;

public class HoldSeatsRequest {

    private Long userId;
    private Long tripId;
    private List<Long> seatIds;
    private double totalAmount;
    private String contact; // Shared contact for all passengers
    private List<PassengerDTO> passengers;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }
    public List<Long> getSeatIds() { return seatIds; }
    public void setSeatIds(List<Long> seatIds) { this.seatIds = seatIds; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public List<PassengerDTO> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerDTO> passengers) { this.passengers = passengers; }
}
