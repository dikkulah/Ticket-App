package com.payment.service;

import com.payment.dto.PaymentDto;
import com.payment.model.Payment;
import com.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<PaymentDto> createOrSavePayment(PaymentDto request) {
        log.info("payment service,createOrSavePayment ");

        return ResponseEntity.ok().body(modelMapper.map(paymentRepository.save(modelMapper.map(request, Payment.class)),PaymentDto.class));
    }


    public ResponseEntity<PaymentDto> getPaymentOfTicket(String email, Long tripId, Integer seatNo) {
        log.info("payment service,getPaymentOfTicket ");

        return ResponseEntity.ok().body(modelMapper.map(paymentRepository.findPaymentByUserEmailAndTripIdAndSeatNo(email, tripId, seatNo), PaymentDto.class));

    }

    public ResponseEntity<List<PaymentDto>> getPaymentOfTripByEmailAndTripId(Long tripId) {
        log.info("payment service,getPaymentOfTripByEmailAndTripId ");

        return ResponseEntity.ok().body(paymentRepository.findPaymentsByTripId(tripId)
                .stream().map(payment -> modelMapper.map(payment, PaymentDto.class)).toList());
    }
}

