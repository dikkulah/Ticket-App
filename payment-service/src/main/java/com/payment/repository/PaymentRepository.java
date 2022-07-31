package com.payment.repository;

import com.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Payment findPaymentByUserEmailAndTripIdAndSeatNo(String userEmail, Long tripId, Integer seatNo);
    List<Payment> findPaymentsByUserEmailAndTripId(String userEmail, Long tripId);
}
