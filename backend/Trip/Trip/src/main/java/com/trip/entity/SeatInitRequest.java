package com.trip.entity;

public class SeatInitRequest {

    private Long tripId;
    private BusDetails busDetails;

    // Constructors
    public SeatInitRequest() {
    }

    public SeatInitRequest(Long tripId, BusDetails busDetails) {
        this.tripId = tripId;
        this.busDetails = busDetails;
    }

    // Getters and setters
    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public BusDetails getBusDetails() {
        return busDetails;
    }

    public void setBusDetails(BusDetails busDetails) {
        this.busDetails = busDetails;
    }
}

