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


    @PostMapping("buy/{userEmail}/{tripId}")
    public ResponseEntity<List<TicketDto>> buyTickets(@RequestBody List<TicketDto> ticketDtos, @PathVariable String userEmail, @PathVariable Long tripId){
        return ticketService.buyTickets(ticketDtos, userEmail,tripId);
    }








}
