package com.ticket.controller;

import com.ticket.dto.TripDto;
import com.ticket.model.enums.Station;
import com.ticket.model.enums.VehicleType;
import com.ticket.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("trip")
@RequiredArgsConstructor
@Slf4j
public class TripController {

    private final TripService tripService;

    @GetMapping
    public ResponseEntity<List<TripDto>> getTripByPropertiesOrAll(@RequestParam(value = "vehicleType", required = false) VehicleType vehicleType
            , @RequestParam(value = "to", required = false) Station to
            , @RequestParam(value = "from", required = false) Station from
            , @RequestParam(value = "arrivalTime", required = false) String arrivalTimeString
            , @RequestParam(value = "departureTime", required = false) String departureTimeString) {
        return tripService.getTripByPropertiesOrAll(vehicleType, to, from, arrivalTimeString, departureTimeString);
    }

    @GetMapping("{id}")
    public ResponseEntity<TripDto> getTripById(@PathVariable Long id){
        return tripService.getTripById(id);
    }


    @PostMapping("{email}")
    public ResponseEntity<TripDto> addTrip(@RequestBody TripDto request, @PathVariable String email) {
        return tripService.addTrip(request, email);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> cancelTrip(@PathVariable Long id) {
        return tripService.cancelTrip(id);
    }


}
