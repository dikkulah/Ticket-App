package com.ticket.dto;


import com.ticket.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto implements Serializable {
    private Long id;
    private Integer seatNo;
    private BigDecimal ticketPrice;
    private TripDto trip;
    private String fullName;
    private Gender gender;
    private PaymentDto payment;
}
