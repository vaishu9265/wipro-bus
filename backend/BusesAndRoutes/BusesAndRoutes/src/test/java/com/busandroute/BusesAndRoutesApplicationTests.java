package com.busandroute;

import com.busandroute.entity.Bus;
import com.busandroute.entity.Route;
import com.busandroute.repository.BusRepository;
import com.busandroute.repository.RouteRepository;
import com.busandroute.service.BusService;
import com.busandroute.service.RouteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusAndRouteServiceTests {

    // Bus Service + Repo Mocks
    @Mock
    private BusRepository busRepository;

    @InjectMocks
    private BusService busService;

    // Route Service + Repo Mocks
    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteService routeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // BusService tests

    @Test
    void createBus_Success() {
        Bus bus = new Bus();
        bus.setBusNumber("BUS123");
        when(busRepository.save(bus)).thenReturn(bus);

        Bus created = busService.createBus(bus);
        assertEquals("BUS123", created.getBusNumber());
        verify(busRepository).save(bus);
    }

    @Test
    void getAllBuses_ReturnsList() {
        List<Bus> buses = Arrays.asList(new Bus(), new Bus());
        when(busRepository.findAll()).thenReturn(buses);

        List<Bus> result = busService.getAllBuses();
        assertEquals(2, result.size());
    }

    @Test
    void getBusById_Found() {
        Bus bus = new Bus();
        bus.setBusNumber("BUS456");
        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));

        Optional<Bus> result = busService.getBusById(1L);
        assertTrue(result.isPresent());
        assertEquals("BUS456", result.get().getBusNumber());
    }

    @Test
    void updateBus_Success() {
        Bus existing = new Bus();
        existing.setBusNumber("OLD123");
        when(busRepository.findById(1L)).thenReturn(Optional.of(existing));

        Bus updatedDetails = new Bus();
        updatedDetails.setBusNumber("NEW123");
        updatedDetails.setBusType("AC");
        updatedDetails.setTotalSeats(50);
        updatedDetails.setOperatorName("OperatorX");

        when(busRepository.save(existing)).thenReturn(existing);

        Bus updated = busService.updateBus(1L, updatedDetails);

        assertEquals("NEW123", updated.getBusNumber());
        assertEquals("AC", updated.getBusType());
        assertEquals(50, updated.getTotalSeats());
        assertEquals("OperatorX", updated.getOperatorName());
    }

    @Test
    void updateBus_NotFound_Throws() {
        when(busRepository.findById(1L)).thenReturn(Optional.empty());
        Bus details = new Bus();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> busService.updateBus(1L, details));
        assertTrue(ex.getMessage().contains("Bus not found"));
    }

    @Test
    void deleteBus_CallsRepository() {
        doNothing().when(busRepository).deleteById(1L);
        busService.deleteBus(1L);
        verify(busRepository).deleteById(1L);
    }

    // RouteService tests

    @Test
    void createRoute_Success() {
        Route route = new Route();
        route.setSource("CityA");
        when(routeRepository.save(route)).thenReturn(route);

        Route created = routeService.createRoute(route);
        assertEquals("CityA", created.getSource());
        verify(routeRepository).save(route);
    }

    @Test
    void getAllRoutes_ReturnsList() {
        List<Route> routes = Arrays.asList(new Route(), new Route());
        when(routeRepository.findAll()).thenReturn(routes);

        List<Route> result = routeService.getAllRoutes();
        assertEquals(2, result.size());
    }

    @Test
    void getRouteById_Found() {
        Route route = new Route();
        route.setSource("CityB");
        when(routeRepository.findById(1L)).thenReturn(Optional.of(route));

        Optional<Route> result = routeService.getRouteById(1L);
        assertTrue(result.isPresent());
        assertEquals("CityB", result.get().getSource());
    }

    @Test
    void updateRoute_Success() {
        Route existing = new Route();
        existing.setSource("OldCity");
        when(routeRepository.findById(1L)).thenReturn(Optional.of(existing));

        Route details = new Route();
        details.setSource("NewCity");
        details.setDestination("DestCity");
        details.setDistance(100);
        details.setDuration(120);

        when(routeRepository.save(existing)).thenReturn(existing);

        Route updated = routeService.updateRoute(1L, details);

        assertEquals("NewCity", updated.getSource());
        assertEquals("DestCity", updated.getDestination());
        assertEquals(100, updated.getDistance());
        assertEquals(120, updated.getDuration());
    }

    @Test
    void updateRoute_NotFound_Throws() {
        when(routeRepository.findById(1L)).thenReturn(Optional.empty());
        Route details = new Route();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> routeService.updateRoute(1L, details));
        assertTrue(ex.getMessage().contains("Route not found"));
    }

    @Test
    void deleteRoute_CallsRepository() {
        doNothing().when(routeRepository).deleteById(1L);
        routeService.deleteRoute(1L);
        verify(routeRepository).deleteById(1L);
    }

    @Test
    void searchRoutesByOriginAndDestination_ReturnsMatching() {
        Route route1 = new Route();
        route1.setSource("CityA");
        route1.setDestination("CityB");

        List<Route> routes = Arrays.asList(route1);

        when(routeRepository.findBySourceIgnoreCaseAndDestinationIgnoreCase("CityA", "CityB")).thenReturn(routes);

        List<Route> result = routeService.searchRoutesByOriginAndDestination("CityA", "CityB");
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("CityA", result.get(0).getSource());
    }
}

