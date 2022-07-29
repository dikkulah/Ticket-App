package com.payment.service;

import com.payment.dto.PaymentDto;
import com.payment.model.Payment;
import com.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    public PaymentDto createPayment(PaymentDto request) {
        log.info("Ödeme servisine ulaştı");
        paymentRepository.save(modelMapper.map(request, Payment.class));
        return request;
    }

    public List<PaymentDto> getPaymentByEmail(String email) {
        log.info("Ödeme görütnüleme servisine ulaştı");
        return mapList(paymentRepository.findPaymentsByUserEmail(email), PaymentDto.class);
    }

    <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .toList();
    }
}

