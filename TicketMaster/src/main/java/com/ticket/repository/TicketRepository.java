package com.ticket.repository;

import com.ticket.model.Ticket;
import com.ticket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    List<Ticket> findTicketsByUser(User user);
}
