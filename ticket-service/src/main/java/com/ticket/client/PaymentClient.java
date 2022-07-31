package com.ticket.client;

import com.ticket.dto.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "payment-service", url = "http://localhost:8082/payment")
public interface PaymentClient {
    @PostMapping
    ResponseEntity<PaymentDto> createOrSavePayment(@RequestBody PaymentDto request);

    @GetMapping("{email}/{tripId}")
    ResponseEntity<List<PaymentDto>> getPaymentOfTripByEmailAndTripId(@PathVariable String email, @PathVariable Long tripId);

    @GetMapping("{email}/{tripId}/{seatNo}")
    ResponseEntity<PaymentDto> getPaymentOfTicket(@PathVariable String email, @PathVariable Long tripId, @PathVariable Integer seatNo);

}