package com.booking.controller;

import com.booking.entity.HoldSeatsRequest;
import com.booking.entity.Booking;
import com.booking.service.BookingService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/hold")
    public Booking holdSeats(@RequestBody HoldSeatsRequest request) {
        return bookingService.holdSeats(request);
    }

    @PostMapping("/confirm/{bookingId}")
    public Booking confirmBooking(@PathVariable Long bookingId) {
        return bookingService.confirmBooking(bookingId);
    }

    @PostMapping("/cancel/{bookingId}")
    public void cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
    }

    @GetMapping("/user/{userId}")
    public List<Booking> getBookingsByUser(@PathVariable Long userId) {
        return bookingService.getBookingsByUser(userId);
    }
    
    @GetMapping("/admin/bookings-by-trip")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Map<Long, List<Booking>> getBookingsGroupedByTrip() {
        return bookingService.getBookingsGroupedByTrip();
    }
}
