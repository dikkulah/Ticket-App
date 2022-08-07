package com.admin.controller;

import com.admin.dto.AdminDto;
import com.admin.dto.TripDto;
import com.admin.model.enums.Station;
import com.admin.model.enums.Vehicle;
import com.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin")@Slf4j
public class AdminController {

    private final AdminService adminService;
    @PostMapping
    public ResponseEntity<String> registerAdmin(@RequestBody AdminDto adminDto){
        return adminService.register(adminDto);
    }

    @PostMapping("/{email}/{password}")
    public ResponseEntity<String> loginAdmin(@PathVariable String email, @PathVariable String password){
        log.info("admin conroller : login admin");
        return adminService.login(email,password);
    }

    @PostMapping("trip/{email}")
    public ResponseEntity<TripDto> addTrip(@RequestBody TripDto tripDto,@PathVariable String email){
        log.info("admin conroller : add trip");
        return adminService.addTrip(tripDto,email);
    }
    @DeleteMapping("trip/{tripId}/{email}")
    public ResponseEntity<String> cancelTrip(@PathVariable Long tripId,@PathVariable String email){
        log.info("admin conroller : cancel trip");
        return adminService.cancelTrip(tripId,email);
    }
    @GetMapping("total/{email}")
    public ResponseEntity<String> getTotalAndCounts(@PathVariable String email){
        log.info("admin conroller : get total and counts");
        return adminService.getTotalAndCounts(email);
    }
    @GetMapping("trips")
    public ResponseEntity<List<TripDto>> getTripByPropertiesOrAll(@RequestParam(value = "vehicle", required = false) Vehicle vehicle
            , @RequestParam(value = "to", required = false) Station to
            , @RequestParam(value = "from", required = false) Station from
            , @RequestParam(value = "arrivalTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalTime
            , @RequestParam(value = "departureTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime) {
        log.info("admin conroller : get trip by properties");
        return adminService.getTripByPropertiesOrAll(vehicle, to, from, arrivalTime, departureTime);
    }


}
