package com.busandroute.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busandroute.entity.Bus;
import com.busandroute.repository.BusRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BusService {

    @Autowired
    private BusRepository busRepository;

    public Bus createBus(Bus bus) {
        return busRepository.save(bus);
    }

    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    public Optional<Bus> getBusById(Long id) {
        return busRepository.findById(id);
    }

    public Bus updateBus(Long id, Bus busDetails) {
        return busRepository.findById(id).map(bus -> {
            bus.setBusNumber(busDetails.getBusNumber());
            bus.setBusType(busDetails.getBusType());
            bus.setTotalSeats(busDetails.getTotalSeats());
            bus.setOperatorName(busDetails.getOperatorName());
            return busRepository.save(bus);
        }).orElseThrow(() -> new RuntimeException("Bus not found with id " + id));
    }

    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }
}
