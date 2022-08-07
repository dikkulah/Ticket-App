package com.ticket.service;

import com.ticket.dto.TripDto;
import com.ticket.exception.TripNotFoundException;
import com.ticket.model.Trip;
import com.ticket.model.enums.Station;
import com.ticket.model.enums.Vehicle;
import com.ticket.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripService {

    private final TripRepository tripRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<List<TripDto>> getTripByPropertiesOrAll(Vehicle vehicle, Station to, Station from, LocalDateTime arrivalTime, LocalDateTime departureTime) {
        log.info("trip service, getTripByPropertiesOrAll");
        List<Trip> trips = tripRepository.findActiveTripsByProperties(arrivalTime, departureTime, vehicle, to, from);
        return ResponseEntity.ok().body(trips.stream().map(trip -> modelMapper.map(trip, TripDto.class)).toList());
    }


    public ResponseEntity<TripDto> getTripById(Long id) {
        log.info("trip service, getTripById");
        Trip trip = tripRepository.findById(id).orElseThrow(TripNotFoundException::new);
        return ResponseEntity.ok().body(modelMapper.map(trip, TripDto.class));
    }

}
