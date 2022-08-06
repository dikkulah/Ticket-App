package com.ticket.service.strategy.ticket_strategy;

import com.ticket.dto.TicketDto;

import java.util.List;

public interface TicketBuyStrategy {
    boolean checkConditions(List<TicketDto> ticketDtos, int boughtAndWillBeCount);
}
