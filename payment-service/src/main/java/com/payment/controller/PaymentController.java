package com.payment.controller;

import com.payment.dto.PaymentDto;
import com.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    ResponseEntity<PaymentDto> createOrUpdatePayment(@RequestBody PaymentDto request) {
        log.info("payment conroller, createOrUpdatePayment");
        return paymentService.createOrSavePayment(request);
    }


    @GetMapping("{email}/{tripId}/{seatNo}")
    ResponseEntity<PaymentDto> getPaymentOfTicket(@PathVariable String email, @PathVariable Long tripId, @PathVariable Integer seatNo) {
        log.info("payment conroller, getPaymentOfTicket");
        return paymentService.getPaymentOfTicket(email, tripId, seatNo);
    }

    @GetMapping("{tripId}")
    ResponseEntity<List<PaymentDto>> getPaymentsOfTrip(@PathVariable Long tripId) {
        log.info("payment conroller, getPaymentsOfTrip");
        return paymentService.getPaymentOfTripByEmailAndTripId(tripId);
    }


}
