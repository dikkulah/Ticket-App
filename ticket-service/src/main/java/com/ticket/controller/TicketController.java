package com.ticket.controller;

import com.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ticket")
@RequiredArgsConstructor
@Slf4j
public class TicketController {
    private final TicketService ticketService;
    //todo satılan bilet sayısı ve toplam gelir admin yapar.
    //TODO bireysel kullanıcılar aynı sefer için enfazla 5 bilet alabilir
    //TODO Bireysel kullanıcı aynı sefer için 2 erkek bilet alabilir.

    //Todo Kurumsal kullanıcı aynı sefer için 20 bilet alabilir


    //todo bilet alınınca telefona mesaj gönder asenkron




}
