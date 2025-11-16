package com.booking.service;

import com.booking.entity.HoldSeatsRequest;
import com.booking.entity.PassengerDTO;
import com.booking.entity.Booking;
import com.booking.entity.Passenger;
import com.booking.entity.Seat;
import com.booking.repository.BookingRepository;
import com.booking.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    public BookingService(BookingRepository bookingRepository, SeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public Booking holdSeats(HoldSeatsRequest request) {

        // STEP 1: Create empty booking
        Booking booking = new Booking();
        booking.setUserId(request.getUserId());
        booking.setTripId(request.getTripId());
        booking.setBookingDate(new Date());
        booking.setStatus("HOLD");
        booking.setTotalAmount(request.getTotalAmount());

        // STEP 2: Save booking first → generates booking_id
        booking = bookingRepository.save(booking);

        // STEP 3: Update Seats
        List<Seat> selectedSeats = new ArrayList<>();

        for (Long seatNumber : request.getSeatIds()) {

            Seat seat = seatRepository.findByTripIdAndSeatNumber(request.getTripId(), seatNumber)
                    .orElseThrow(() -> new RuntimeException("Seat not found: " + seatNumber));

            if (seat.isBooked()) {
                throw new RuntimeException("Seat already booked: " + seatNumber);
            }

            seat.setBooked(true);
            seat.setBooking(booking);
            selectedSeats.add(seat);
        }

        booking.setSeats(selectedSeats);

        // STEP 4: Add passengers
        List<Passenger> passengerList = new ArrayList<>();

        for (PassengerDTO pDTO : request.getPassengers()) {
            Passenger passenger = new Passenger();
            passenger.setName(pDTO.getName());
            passenger.setAge(pDTO.getAge());
            passenger.setGender(pDTO.getGender());
            passenger.setContact(request.getContact());
            passenger.setBooking(booking);
            passengerList.add(passenger);
        }

        booking.setPassengers(passengerList);

        // STEP 5: Save final booking with seats + passengers
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));
        booking.setStatus("CONFIRMED");
        return bookingRepository.save(booking);
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        // Restore seats to not booked
        for (Seat seat : booking.getSeats()) {
            seat.setBooked(false);
            seat.setBooking(null);
        }

        // Mark booking as cancelled
        booking.setStatus("CANCELLED");

        bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getUserId().equals(userId))
                .toList();
    }
    
    public Map<Long, List<Booking>> getBookingsGroupedByTrip() {
        List<Booking> allBookings = bookingRepository.findAll();
        return allBookings.stream().collect(Collectors.groupingBy(Booking::getTripId));
    }
}
