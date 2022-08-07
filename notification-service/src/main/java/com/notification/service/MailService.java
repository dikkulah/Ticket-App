package com.notification.service;

import com.notification.dto.NotificationDto;
import com.notification.model.Mail;
import com.notification.repository.MailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service@Slf4j
public class MailService implements NotificationService{
    @Autowired
    MailRepository mailRepository;
    @Override
    public void send(NotificationDto dto) {
        log.info("mail service");
        mailRepository.save(new Mail(dto.getTitle(), dto.getTo(), dto.getFrom(),dto.getText(), LocalDateTime.parse(dto.getSendingTime())));
    }
}
