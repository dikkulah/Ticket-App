package com.admin.dto;


import com.admin.model.enums.Station;
import com.admin.model.enums.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripDto implements Serializable {
    private Long id;
    private Vehicle vehicle;
    private BigDecimal ticketPrice;
    private Station fromStation;
    private Station toStation;
    private Integer seatCapacity;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Set<Integer> purchasedSeats;
    private Boolean isCanceled;


}
