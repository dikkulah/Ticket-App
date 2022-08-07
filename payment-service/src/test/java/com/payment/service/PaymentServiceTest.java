package com.payment.service;

import com.payment.dto.PaymentDto;
import com.payment.model.Payment;
import com.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@SpringBootTest
class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;
    @Mock
    private  PaymentRepository paymentRepository;
    @Mock
    private  ModelMapper modelMapper;

    @Test
    void create_payment() {

        PaymentDto request= new PaymentDto();
        Payment payment = modelMapper.map(request,Payment.class);
        when(modelMapper.map(request, Payment.class)).thenReturn(payment);
        when(modelMapper.map(payment, PaymentDto.class)).thenReturn(request);
        when(paymentRepository.save(payment)).thenReturn(payment);
        var response= paymentService.createOrSavePayment(request);
        verify(paymentRepository,times(1)).save(payment);
        assertEquals(request,response.getBody());


    }

    @Test
    void get_payment() {
        PaymentDto request= new PaymentDto();
        Long id = 1L;
        var email = "ufuk@gmail.com";
        Payment payment = modelMapper.map(request,Payment.class);
        when(modelMapper.map(request, Payment.class)).thenReturn(payment);
        when(modelMapper.map(payment, PaymentDto.class)).thenReturn(request);
        when(paymentRepository.findPaymentByUserEmailAndTripIdAndSeatNo(email,id,5)).thenReturn(payment);
        var response= paymentService.getPaymentOfTicket(email,id,5);
        verify(paymentRepository,times(1)).findPaymentByUserEmailAndTripIdAndSeatNo(email,id,5);
        assertEquals(request,response.getBody());
    }

    @Test
    void getPaymentOfTripByEmailAndTripId() {
        PaymentDto request= new PaymentDto();
        Long id = 1L;
        var email = "ufuk@gmail.com";
        Payment payment = modelMapper.map(request,Payment.class);
        var list = new ArrayList<Payment>();
        list.add(payment);
        var listDto = new ArrayList<PaymentDto>();
        listDto.add(request);
        when(modelMapper.map(request, Payment.class)).thenReturn(payment);
        when(modelMapper.map(payment, PaymentDto.class)).thenReturn(request);
        when(paymentRepository.findPaymentsByTripId(id)).thenReturn(list);
        var response=paymentService.getPaymentOfTripByEmailAndTripId(id);

        assertEquals(listDto,response.getBody());

    }
}