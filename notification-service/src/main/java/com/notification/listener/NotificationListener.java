package com.notification.listener;


import com.notification.dto.NotificationDto;
import com.notification.model.enums.NotificationType;
import com.notification.service.MailService;
import com.notification.service.SmsService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j

public class NotificationListener {

    @Autowired
    private SmsService smsService;
    @Autowired
    private MailService mailService;

    @RabbitListener(queues = "ticketMaster")
    public void handleNotification(NotificationDto dto) {
        if (dto.getType() == NotificationType.EMAIL) mailService.send(dto);
        else if (dto.getType() == NotificationType.SMS) smsService.send(dto);
        else throw new IllegalArgumentException("Böyle bir mesaj tipi yok");

    }
}
