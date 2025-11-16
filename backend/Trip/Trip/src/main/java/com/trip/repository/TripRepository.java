package com.trip.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trip.entity.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByRouteIdInAndDepartureTimeBetweenAndCancelledFalse(
        List<Long> routeIds,
        LocalDateTime startOfDay,
        LocalDateTime endOfDay
    );
}


