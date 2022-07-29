package com.payment.model;

import com.payment.dto.enums.PaymentType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Entity
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String userEmail;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private String iban = null;
    private String cardNumber = null;
    private String securityCode = null;
    private Currency currency;
    private BigDecimal amount;
    private LocalDateTime paymentTime;

}
