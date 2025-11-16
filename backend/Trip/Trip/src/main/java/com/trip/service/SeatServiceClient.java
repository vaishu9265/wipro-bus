package com.trip.service;

import com.trip.entity.SeatInitRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "BookingDetails", url = "http://localhost:9004") // Replace with actual URL or use service discovery
public interface SeatServiceClient {

    @PostMapping("/api/v1/seats/init")
    void initSeats(@RequestBody SeatInitRequest request);
}
