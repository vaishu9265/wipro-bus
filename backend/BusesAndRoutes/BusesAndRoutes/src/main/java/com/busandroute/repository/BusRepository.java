package com.busandroute.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busandroute.entity.Bus;

public interface BusRepository extends JpaRepository<Bus, Long> {}

