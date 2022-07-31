package com.payment.service;

import com.payment.dto.PaymentDto;
import com.payment.model.Payment;
import com.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<PaymentDto> createPayment(PaymentDto request) {
        log.info("Ödeme servisine ulaştı");
        paymentRepository.save(modelMapper.map(request, Payment.class));
        return ResponseEntity.ok().body(request);
    }

    public ResponseEntity<List<PaymentDto>> getPaymentByEmail(String email, Long tripId, Integer seatNo) {
        log.info("Ödeme görütnüleme servisine ulaştı");
        if (paymentRepository.findPaymentsByUserEmail(email).isEmpty())
            return ResponseEntity.badRequest().body(new ArrayList<>());
        else
            return ResponseEntity.ok().body(paymentRepository.findPaymentsByUserEmail(email).stream().filter(payment -> Objects.equals(payment.getTripId(), tripId) && Objects.equals(payment.getSeatNo(), seatNo)).map(payment -> modelMapper.map(payment, PaymentDto.class)).toList());
    }

}

