package com.booking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.booking.entity.SeatInitRequest;
import com.booking.service.SeatService;


@RestController
@RequestMapping("/api/v1/seats")
public class SeatInitController {

    private final SeatService seatService;

    public SeatInitController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping("/init")
    public ResponseEntity<Void> initSeats(@RequestBody SeatInitRequest request) {
        seatService.createSeatsForTrip(request.getTripId(), request.getBusDetails());
        return ResponseEntity.ok().build();
    }
}

