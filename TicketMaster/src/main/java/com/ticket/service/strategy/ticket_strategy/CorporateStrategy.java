package com.ticket.service.strategy.ticket_strategy;


import com.ticket.dto.TicketDto;
import com.ticket.exception.TicketBuyCondidationsException;

import java.util.List;


public class CorporateStrategy implements TicketBuyStrategy{


    @Override
    public boolean checkConditions(List<TicketDto> ticketDtos, int boughtAndWillBeCount) {
        if (boughtAndWillBeCount < 20) { //Alınmış ve alınacak biletlerin toplamı 20ye eşit veya azmı ?
            return true;
        } else {
            throw new TicketBuyCondidationsException("Kurumsal kullanıcılar bir seferden 20 den fazla bilet alamazlar.");
        }
    }

}
