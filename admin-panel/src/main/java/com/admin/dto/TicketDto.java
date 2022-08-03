package com.admin.dto;

import com.admin.model.enums.Gender;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TicketDto implements Serializable {
    private Long id;
    private Integer seatNo;
    private TripDto trip;
    private PassengerDto passenger;

    @NotNull
    private PaymentDto payment;

}
