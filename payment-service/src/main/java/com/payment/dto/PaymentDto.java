package com.payment.dto;

import com.payment.dto.enums.PaymentType;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Data
@Builder
public class PaymentDto implements Serializable {
    private String userEmail;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private String iban;
    private String cardNumber;
    private String securityCode;
    private Currency currency;
    private BigDecimal amount;
    private LocalDateTime paymentTime;


}
