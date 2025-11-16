package com.trip.entity;

public class BusDetails {

    private int totalSeats;
    private int seatsPerRow;

    // Constructors
    public BusDetails() {
    }

    public BusDetails(int totalSeats, int seatsPerRow) {
        this.totalSeats = totalSeats;
        this.seatsPerRow = seatsPerRow;
    }

    // Getters and setters
    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(int seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }
}
