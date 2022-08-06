package com.notification.service;

import com.notification.dto.NotificationDto;
import com.notification.model.Mail;
import com.notification.repository.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MailService implements NotificationService{
    @Autowired
    MailRepository mailRepository;
    @Override
    public void send(NotificationDto dto) {
        mailRepository.save(new Mail(dto.getTitle(), dto.getTo(), dto.getFrom(),dto.getText(), LocalDateTime.parse(dto.getSendingTime())));
    }
}
