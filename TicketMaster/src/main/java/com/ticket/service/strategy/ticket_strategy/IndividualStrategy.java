package com.ticket.service.strategy.ticket_strategy;


import com.ticket.dto.TicketDto;
import com.ticket.exception.TicketBuyCondidationsException;
import com.ticket.model.enums.Gender;

import java.util.List;

public class IndividualStrategy implements TicketBuyStrategy{


    @Override
    public boolean checkConditions(List<TicketDto> ticketDtos, int boughtAndWillBeCount) {
        if (boughtAndWillBeCount <= 5) { // Alınmış ve alınacak biletlerin toplamı 5e eşit veya azmı ?
            if (ticketDtos.stream().filter(ticketDto -> ticketDto.getPassenger().getGender() == Gender.MALE).count() <= 2) {//aynı anda alınan biletlerde 2 den fazla erkek varmı ?
                return true;
            } else
                throw new TicketBuyCondidationsException("Bireysel kullanıcılar bir siparişte en fazla 2 erkek yolcu için bilet alabilir");
        } else {
            throw new TicketBuyCondidationsException("Bireysel kullanıcılar bir seferden 5 den fazla bilet alamazlar.");
        }
    }


}
