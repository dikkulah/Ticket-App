package com.notification.service;

import com.notification.dto.NotificationDto;
import com.notification.model.Sms;
import com.notification.repository.SmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SmsService  implements NotificationService{
    @Autowired
    SmsRepository smsRepository;
    @Override
    public void send(NotificationDto dto) {
        smsRepository.save(new Sms( dto.getTo(), dto.getFrom(),dto.getText(), LocalDateTime.parse(dto.getSendingTime())));

    }
}
