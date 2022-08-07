package com.ticket.service;

import com.ticket.exception.TripNotFoundException;
import com.ticket.model.Trip;
import com.ticket.model.enums.Station;
import com.ticket.model.enums.Vehicle;
import com.ticket.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class TripServiceTest {

    @InjectMocks
    private TripService tripService;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void get_trips_by_properties() {
        var time = LocalDateTime.now();
        var vehicle= Vehicle.BUS;
        var station = Station.ANKARA;
        var list = new ArrayList<Trip>();
        list.add(new Trip());
        when(tripRepository.findActiveTripsByProperties(time, time,vehicle, station, station))
                .thenReturn(list);
        var response= tripService.getTripByPropertiesOrAll(vehicle,station,station,time,time);
        verify(tripRepository,times(1)).findActiveTripsByProperties(time, time,vehicle, station, station);
        assertEquals(tripService.getTripByPropertiesOrAll(vehicle,station,station,time,time),response);
    }
    @Test
    void get_trip_by_id() {
        Long id = 15L;
        when(tripRepository.findById(id)).thenReturn(Optional.of(mock(Trip.class)));
        var response= tripService.getTripById(id);

        verify(tripRepository,times(1)).findById(id);

        assertEquals(tripService.getTripById(id),response);

    }

    @Test
    void cant_get_trip_by_id() {

        Long id = 15L;
        when(tripRepository.findById(id)).thenReturn(Optional.empty()).thenThrow(TripNotFoundException.class);
        assertThrows(TripNotFoundException.class,()->tripService.getTripById(id));
        verify(tripRepository,times(1)).findById(id);

    }


}