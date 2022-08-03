package com.ticket.controller;

import com.ticket.dto.TripDto;
import com.ticket.model.enums.Station;
import com.ticket.model.enums.Vehicle;
import com.ticket.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("trip")
@RequiredArgsConstructor
@Slf4j
public class TripController {

    private final TripService tripService;


    @GetMapping
    public ResponseEntity<List<TripDto>> getTripByPropertiesOrAll(@RequestParam(value = "vehicle", required = false) Vehicle vehicle
            , @RequestParam(value = "to", required = false) Station to
            , @RequestParam(value = "from", required = false) Station from
            , @RequestParam(value = "arrivalTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalTime
            , @RequestParam(value = "departureTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime) {

        return tripService.getTripByPropertiesOrAll(vehicle, to, from, arrivalTime, departureTime);
    }

    @GetMapping("{id}")
    public ResponseEntity<TripDto> getTripById(@PathVariable Long id){
        return tripService.getTripById(id);
    }


//    @PostMapping("{email}")
//    public ResponseEntity<TripDto> addTrip(@RequestBody TripDto request, @PathVariable String email) {
//        return tripService.addTrip(request, email);
//    }
//
//    @DeleteMapping("{tripId}")
//    public ResponseEntity<String> cancelTrip(@PathVariable Long tripId, String email) {
//        return tripService.cancelTrip(tripId, email);
//    }


}
