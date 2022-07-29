package com.ticket.service;

import com.ticket.dto.TripDto;
import com.ticket.model.Trip;
import com.ticket.model.User;
import com.ticket.model.enums.RoleType;
import com.ticket.model.enums.Station;
import com.ticket.model.enums.VehicleType;
import com.ticket.repository.TripRepository;
import com.ticket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<List<TripDto>> getTripByPropertiesOrAll(VehicleType vehicleType, Station to, Station from, String arrivalTimeString, String departureTimeString) {
        return ResponseEntity.ok().body(tripRepository.findTripsByProperties(LocalDateTime.parse(arrivalTimeString), LocalDateTime.parse(departureTimeString), vehicleType, to, from)
                .stream().map(trip -> modelMapper.map(trip, TripDto.class)).toList());
    }


    public ResponseEntity<TripDto> addTrip(TripDto request, String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bu maile kayıtlı bir kullanıcı yok."));
        if (user.getRoleType() == RoleType.ADMIN) {
            Trip trip = modelMapper.map(request, Trip.class);
            trip.setSeatCapacity(switch (trip.getVehicleType()) {
                case PLANE -> 189;
                case BUS -> 49;
            });
            return ResponseEntity.ok().body(modelMapper.map(tripRepository.save(trip), TripDto.class));
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sefer eklemek için yetkiniz yok.");
    }

    public ResponseEntity<String> cancelTrip(Long id) {
        // Sadece admin yapabilir.
        return null;
    }

    public ResponseEntity<TripDto> getTripById(Long id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Böyle bir sefer yok."));
        return ResponseEntity.ok().body(modelMapper.map(trip, TripDto.class));
    }
}
