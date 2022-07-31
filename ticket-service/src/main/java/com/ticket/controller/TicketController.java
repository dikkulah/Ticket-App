package com.ticket.controller;

import com.ticket.dto.TicketDto;
import com.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ticket")
@RequiredArgsConstructor
@Slf4j
public class TicketController {
    private final TicketService ticketService;
    @GetMapping("totalAndCount/{email}")
    public ResponseEntity<String> getTotalAndCounts(@PathVariable String email){
        return ticketService.getTotalAndCounts(email);
    }

    @PostMapping("buy/{email}/{tripId}")
    public ResponseEntity<List<TicketDto>> buyTickets(@RequestBody List<TicketDto> ticketDtos,@PathVariable String email,@PathVariable Long tripId){
        return ticketService.buyTickets(ticketDtos,email,tripId);
    }








}
