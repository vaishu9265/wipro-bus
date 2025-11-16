package com.busandroute.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busandroute.entity.Route;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findBySourceIgnoreCaseAndDestinationIgnoreCase(String source, String destination);

}
