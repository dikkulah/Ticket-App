package com.ticket.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto implements Serializable {
    private Long id;
    private Integer seatNo;
    private TripDto trip;
    private PassengerDto passenger;
    @NotNull
    private PaymentDto payment;
}
