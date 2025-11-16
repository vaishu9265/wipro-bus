package com.booking.repository;


import com.booking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTripId(Long tripId);
    List<Seat> findByTripIdAndIsBooked(Long tripId, boolean isBooked);
    Optional<Seat> findByTripIdAndSeatNumber(Long tripId, Long seatNumber);

}

