package com.admin.dto;

import com.admin.dto.enums.Currency;
import com.admin.dto.enums.PaymentType;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentDto implements Serializable {
    private Long id;
    private String userEmail;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private Long tripId;
    private Integer seatNo;
    private String iban;
    private String cardNumber;
    private String securityCode;
    private Currency currency;
    private BigDecimal amount;
    private LocalDateTime paymentTime;
    private Boolean isCanceled;




}
