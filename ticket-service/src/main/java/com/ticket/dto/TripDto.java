package com.ticket.dto;


import com.ticket.model.enums.Station;
import com.ticket.model.enums.Vehicle;
import lombok.Data;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class TripDto implements Serializable {
    private Long id;
    private Vehicle vehicle;
    private Station fromStation;
    private Station toStation;
    private Integer seatCapacity;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Set<Integer> purchasedSeats ;
    private Boolean isCanceled;





}
