package com.trip.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.trip.entity.Bus;
import com.trip.entity.Route;

@FeignClient(name = "BusesAndRoutes", url = "http://localhost:9002")
public interface BusClient {

    @GetMapping("/api/v1/buses/{id}")
    Bus getBusById(@PathVariable("id") Long id);
    
    @GetMapping("/api/v1/routes/{id}")
    Route getRouteById(@PathVariable("id") Long id);
   
    @GetMapping("/api/v1/routes/search")
    List<Route> searchRoutes(@RequestParam("origin") String origin, @RequestParam("destination") String destination);

    

}

