package com.busandroute.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busandroute.entity.Route;
import com.busandroute.repository.RouteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }

    public Route updateRoute(Long id, Route routeDetails) {
        return routeRepository.findById(id).map(route -> {
            route.setSource(routeDetails.getSource());
            route.setDestination(routeDetails.getDestination());
            route.setDistance(routeDetails.getDistance());
            route.setDuration(routeDetails.getDuration());
            return routeRepository.save(route);
        }).orElseThrow(() -> new RuntimeException("Route not found with id " + id));
    }

    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }

    // New method to search routes by origin and destination
    public List<Route> searchRoutesByOriginAndDestination(String origin, String destination) {
        return routeRepository.findBySourceIgnoreCaseAndDestinationIgnoreCase(origin, destination);
    }
}
