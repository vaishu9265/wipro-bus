package com.trip.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collections;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trip.entity.Trip;
import com.trip.entity.Bus;
import com.trip.entity.BusDetails;
import com.trip.entity.Route;
import com.trip.entity.SeatInitRequest;
import com.trip.repository.TripRepository;

import jakarta.transaction.Transactional;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private BusClient busClient;
    
    @Autowired
    private SeatServiceClient seatServiceClient;


    @Transactional
    public Trip createTrip(Trip trip) {
        // Validate and enrich bus and route data
        Long busId = trip.getBusId();
        Long routeId = trip.getRouteId();

        // Fetch bus and route details from other microservices
        Bus bus = busClient.getBusById(busId);
        Route route = busClient.getRouteById(routeId);

        trip.setBusId(bus.getId());
        trip.setRouteId(route.getId());

        // Save the trip to generate Trip ID
        Trip savedTrip = tripRepository.save(trip);

        // Create BusDetails DTO containing seat count, seats per row, etc.
        BusDetails busDetails = new BusDetails();
        busDetails.setTotalSeats(bus.getTotalSeats());
        busDetails.setSeatsPerRow(4);

        // Call Seat microservice to initialize all seats for this trip
        SeatInitRequest seatInitRequest = new SeatInitRequest(savedTrip.getId(), busDetails);
        seatServiceClient.initSeats(seatInitRequest);

        return savedTrip;
    }
    
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Optional<Trip> getTripById(Long id) {
        return tripRepository.findById(id);
    }

    public List<Trip> searchTrips(String origin, String destination, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1);

        // Call Route microservice to get matching routes
        List<Route> matchingRoutes = busClient.searchRoutes(origin, destination); // Implement this Feign client method

        List<Long> routeIds = matchingRoutes.stream()
                                .map(Route::getId)
                                .collect(Collectors.toList());

        if (routeIds.isEmpty()) {
            return Collections.emptyList();
        }

        // Query trips by route IDs and departure time range
        return tripRepository.findByRouteIdInAndDepartureTimeBetweenAndCancelledFalse(routeIds, startOfDay, endOfDay);
    }


    public Trip updateTrip(Long id, Trip updatedTrip) {
        return tripRepository.findById(id).map(trip -> {
            Long busId = updatedTrip.getBusId();
            Long routeId = updatedTrip.getRouteId();

            Bus bus = busClient.getBusById(busId);  // Optional validation or enrichment
            Route route = busClient.getRouteById(routeId);

            trip.setBusId(bus.getId());
            trip.setRouteId(route.getId());
            trip.setDepartureTime(updatedTrip.getDepartureTime());
            trip.setArrivalTime(updatedTrip.getArrivalTime());
            trip.setFare(updatedTrip.getFare());
            trip.setCancelled(updatedTrip.isCancelled());

            return tripRepository.save(trip);
        }).orElseThrow(() -> new RuntimeException("Trip not found"));
    }

    public void deleteTrip(Long id) {
    	   Trip trip = tripRepository.findById(id)
    	        .orElseThrow(() -> new RuntimeException("Trip not found"));
    	    trip.setCancelled(true);
    	    tripRepository.save(trip);
    }
}
