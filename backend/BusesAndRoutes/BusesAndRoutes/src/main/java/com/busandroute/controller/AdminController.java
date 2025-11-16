package com.busandroute.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.busandroute.entity.Bus;
import com.busandroute.entity.Route;
import com.busandroute.service.BusService;
import com.busandroute.service.RouteService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class AdminController {

    @Autowired
    private BusService busService;

    @Autowired
    private RouteService routeService;

    // Bus endpoints

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/buses")
    public ResponseEntity<Bus> createBus(@RequestBody Bus bus) {
        Bus savedBus = busService.createBus(bus);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBus);
    }

    @GetMapping("/buses")
    public ResponseEntity<List<Bus>> getAllBuses() {
        List<Bus> buses = busService.getAllBuses();
        return ResponseEntity.ok(buses);
    }

    @GetMapping("/buses/{id}")
    public ResponseEntity<Bus> getBusById(@PathVariable Long id) {
        return busService.getBusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/buses/{id}")
    public ResponseEntity<Bus> updateBus(@PathVariable Long id, @RequestBody Bus bus) {
        Bus updatedBus = busService.updateBus(id, bus);
        return ResponseEntity.ok(updatedBus);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/buses/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return ResponseEntity.noContent().build();
    }

    // Route endpoints

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/routes")
    public ResponseEntity<Route> createRoute(@RequestBody Route route) {
        Route savedRoute = routeService.createRoute(route);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoute);
    }

    @GetMapping("/routes")
    public ResponseEntity<List<Route>> getAllRoutes() {
        List<Route> routes = routeService.getAllRoutes();
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/routes/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        return routeService.getRouteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // New search routes endpoint for origin and destination
    @GetMapping("/routes/search")
    public ResponseEntity<List<Route>> searchRoutes(@RequestParam String origin, @RequestParam String destination) {
        List<Route> routes = routeService.searchRoutesByOriginAndDestination(origin, destination);
        return ResponseEntity.ok(routes);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/routes/{id}")
    public ResponseEntity<Route> updateRoute(@PathVariable Long id, @RequestBody Route route) {
        Route updatedRoute = routeService.updateRoute(id, route);
        return ResponseEntity.ok(updatedRoute);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/routes/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
}
