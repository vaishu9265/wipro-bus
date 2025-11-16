package com.booking.repository;

import com.booking.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}

