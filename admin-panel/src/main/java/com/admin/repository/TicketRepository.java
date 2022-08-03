package com.admin.repository;

import com.admin.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("select sum(m.trip.ticketPrice) from Ticket m")
    BigDecimal totalPrice();
}
