package com.booking.service;

import com.booking.entity.BusDetails;
import com.booking.entity.Seat;
import com.booking.repository.SeatRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getSeatsByTripId(Long tripId) {
        return seatRepository.findByTripId(tripId);
    }

    public Seat getSeatById(Long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
    }

    public Seat save(Seat seat) {
        return seatRepository.save(seat);
    }

    @Transactional
    public void createSeatsForTrip(Long tripId, BusDetails busDetails) {
        int totalSeats = busDetails.getTotalSeats();
        int seatsPerRow = busDetails.getSeatsPerRow();

        List<Seat> seats = new ArrayList<>();
        for (long i = 1; i <= totalSeats; i++) { 
            Seat seat = new Seat();
            seat.setTripId(tripId);
            seat.setSeatNumber(i);  
            if (i % seatsPerRow == 1 || i % seatsPerRow == 0) {
                seat.setSeatType("window");
            } else {
                seat.setSeatType("aisle");
            }
            seat.setBooked(false);  // setBooked method assumed to exist for boolean field isBooked
            seats.add(seat);
        }
        seatRepository.saveAll(seats);
    }
}
