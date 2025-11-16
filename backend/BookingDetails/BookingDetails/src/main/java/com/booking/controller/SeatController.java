package com.booking.controller;

import com.booking.entity.Seat;
import com.booking.service.SeatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/trips")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/{tripId}")
    public List<Seat> getSeatsByTrip(@PathVariable Long tripId) {
        return seatService.getSeatsByTripId(tripId);
    }
}
