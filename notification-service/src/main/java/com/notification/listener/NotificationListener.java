package com.notification.listener;

import com.notification.model.Mail;
import com.notification.model.Sms;
import com.ticket.dto.MailDto;
import com.ticket.dto.SmsDto;
import com.notification.repository.MailRepository;
import com.notification.repository.SmsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@RabbitListener(queues = "ticketMaster")
public class NotificationListener {
    private final MailRepository mailRepository;
    private final SmsRepository smsRepository;
    private final ModelMapper modelMapper;

    @RabbitHandler
    public void handleSms(SmsDto smsDto){
        log.info("sms geldi");
        smsRepository.save(modelMapper.map(smsDto, Sms.class)) ;
    }
    @RabbitHandler
    public void handleMail(MailDto mailDto){
        log.info("mail geldi");
        mailRepository.save(modelMapper.map(mailDto, Mail.class)) ;
    }
}
