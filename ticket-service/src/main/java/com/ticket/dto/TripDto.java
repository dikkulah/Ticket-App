package com.ticket.dto;

import com.ticket.model.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripDto implements Serializable {
    private Long id;
    private VehicleType vehicleType;
    @NotEmpty
    private StationDto from;
    @NotEmpty
    private StationDto to;

    @NotEmpty
    private LocalDateTime departureTime;
    @NotEmpty
    private LocalDateTime arrivalTime;
    private Set<Integer> purchasedSeats ;

}
