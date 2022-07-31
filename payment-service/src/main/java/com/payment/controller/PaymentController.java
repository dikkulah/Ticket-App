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
    ResponseEntity<PaymentDto> createOrSavePayment(@RequestBody PaymentDto request){
        log.info("Ödeme oluşturma controllorına ulaştı.");
        return paymentService.createOrSavePayment(request);
    }


    @GetMapping("{email}/{tripId}")
    ResponseEntity<List<PaymentDto>> getPaymentOfTripByEmailAndTripId(@PathVariable String email,@PathVariable Long tripId){
        log.info("Ödeme görüntüleme controllorına ulaştı.");
        return paymentService.getPaymentOfTripByEmailAndTripId(email,tripId);
    }
    @GetMapping("{email}/{tripId}/{seatNo}")
    ResponseEntity<PaymentDto> getPaymentOfTicket(@PathVariable String email,@PathVariable Long tripId,@PathVariable Integer seatNo){
        log.info("Ödeme görüntüleme controllorına ulaştı.");
        return paymentService.getPaymentOfTicket(email,tripId,seatNo);
    }


}
