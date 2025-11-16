package com.trip;

import com.trip.entity.*;
import com.trip.repository.TripRepository;
import com.trip.service.BusClient;
import com.trip.service.SeatServiceClient;
import com.trip.service.TripService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private BusClient busClient;

    @Mock
    private SeatServiceClient seatServiceClient;

    @InjectMocks
    private TripService tripService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test createTrip method
    @Test
    void createTrip_Success() {
        Trip trip = new Trip();
        trip.setBusId(1L);
        trip.setRouteId(2L);
        trip.setDepartureTime(LocalDateTime.now().plusDays(1));

        Bus bus = new Bus();
        bus.setId(1L);
        bus.setTotalSeats(40);

        Route route = new Route();
        route.setId(2L);

        when(busClient.getBusById(1L)).thenReturn(bus);
        when(busClient.getRouteById(2L)).thenReturn(route);
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> {
            Trip t = invocation.getArgument(0);
            t.setId(100L);
            return t;
        });

        ArgumentCaptor<SeatInitRequest> seatInitCaptor = ArgumentCaptor.forClass(SeatInitRequest.class);

        Trip createdTrip = tripService.createTrip(trip);

        assertNotNull(createdTrip);
        assertEquals(100L, createdTrip.getId());
        verify(seatServiceClient, times(1)).initSeats(seatInitCaptor.capture());
        SeatInitRequest request = seatInitCaptor.getValue();
        assertEquals(100L, request.getTripId());
        assertEquals(40, request.getBusDetails().getTotalSeats());
        assertEquals(4, request.getBusDetails().getSeatsPerRow());
    }

    // Test getAllTrips
    @Test
    void getAllTrips_ReturnsList() {
        Trip t1 = new Trip();
        Trip t2 = new Trip();
        when(tripRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Trip> trips = tripService.getAllTrips();
        assertEquals(2, trips.size());
    }

    // Test getTripById found
    @Test
    void getTripById_Found() {
        Trip trip = new Trip();
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        Optional<Trip> result = tripService.getTripById(1L);
        assertTrue(result.isPresent());
    }

    // Test getTripById not found
    @Test
    void getTripById_NotFound() {
        when(tripRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Trip> result = tripService.getTripById(1L);
        assertFalse(result.isPresent());
    }

    // Test searchTrips with matching routes and trips
    @Test
    void searchTrips_MatchingResults() {
        Route route1 = new Route();
        route1.setId(10L);
        when(busClient.searchRoutes("A", "B")).thenReturn(Arrays.asList(route1));

        LocalDate date = LocalDate.now();

        List<Trip> trips = Collections.singletonList(new Trip());

        when(tripRepository.findByRouteIdInAndDepartureTimeBetweenAndCancelledFalse(
                anyList(), any(LocalDateTime.class), any(LocalDateTime.class)
        )).thenReturn(trips);

        List<Trip> result = tripService.searchTrips("A", "B", date);
        assertEquals(1, result.size());
    }

    // Test searchTrips with no matching routes
    @Test
    void searchTrips_NoMatchingRoutes_ReturnsEmpty() {
        when(busClient.searchRoutes("A", "B")).thenReturn(Collections.emptyList());

        List<Trip> result = tripService.searchTrips("A", "B", LocalDate.now());
        assertTrue(result.isEmpty());
    }

    // Test updateTrip success
    @Test
    void updateTrip_Success() {
        Trip existingTrip = new Trip();
        existingTrip.setId(1L);
        existingTrip.setBusId(1L);
        existingTrip.setRouteId(2L);

        Trip updatedTrip = new Trip();
        updatedTrip.setBusId(3L);
        updatedTrip.setRouteId(4L);
        updatedTrip.setDepartureTime(LocalDateTime.now().plusDays(2));
        updatedTrip.setArrivalTime(LocalDateTime.now().plusDays(3));
        updatedTrip.setCancelled(true);

        Bus newBus = new Bus();
        newBus.setId(3L);
        Route newRoute = new Route();
        newRoute.setId(4L);

        when(tripRepository.findById(1L)).thenReturn(Optional.of(existingTrip));
        when(busClient.getBusById(3L)).thenReturn(newBus);
        when(busClient.getRouteById(4L)).thenReturn(newRoute);
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trip result = tripService.updateTrip(1L, updatedTrip);

        assertEquals(3L, result.getBusId());
        assertEquals(4L, result.getRouteId());
        assertTrue(result.isCancelled());
        verify(tripRepository).save(existingTrip);
    }

    // Test updateTrip not found
    @Test
    void updateTrip_NotFound_ThrowsException() {
        when(tripRepository.findById(1L)).thenReturn(Optional.empty());
        Trip updatedTrip = new Trip();
        RuntimeException ex = assertThrows(RuntimeException.class, () -> tripService.updateTrip(1L, updatedTrip));
        assertEquals("Trip not found", ex.getMessage());
    }

    // Test deleteTrip
    @Test
    void deleteTrip_MarksTripAsCancelled() {
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setCancelled(false);

        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tripService.deleteTrip(1L);

        assertTrue(trip.isCancelled());
        verify(tripRepository).save(trip);
    }

    @Test
    void deleteTrip_TripNotFound_ThrowsException() {
        when(tripRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> tripService.deleteTrip(1L));
        assertEquals("Trip not found", exception.getMessage());
    }

}

