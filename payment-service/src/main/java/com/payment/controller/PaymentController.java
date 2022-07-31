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
    ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto request){
        log.info("Ödeme oluşturma controllorına ulaştı.");
        return paymentService.createPayment(request);
    }


    @GetMapping("{email}/{tripId}/{seatNo}")
    ResponseEntity<List<PaymentDto>> getPayment(@PathVariable String email,@PathVariable Long tripId,@PathVariable Integer seatNo){
        log.info("Ödeme görüntüleme controllorına ulaştı.");
        return paymentService.getPaymentByEmail(email,tripId,seatNo);
    }


}
