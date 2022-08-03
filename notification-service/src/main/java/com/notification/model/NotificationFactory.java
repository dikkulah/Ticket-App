package com.notification.model;


import com.notification.dto.NotificationDto;
import com.notification.dto.NotificationType;
import com.notification.repository.MailRepository;
import com.notification.repository.SmsRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class NotificationFactory {
    public void produceNotification(NotificationDto dto, SmsRepository smsRepository, MailRepository mailRepository) {
        if (dto.getType() == NotificationType.EMAIL) {
            mailRepository.save(new Mail(dto.getTitle(), dto.getTo(), dto.getFrom(), dto.getText(), LocalDateTime.parse(dto.getSendingTime())));
            log.info("Email üretildi ve kaydedildi.");
        } else if (dto.getType() == NotificationType.SMS) {
            smsRepository.save(new Sms(dto.getTo(), dto.getFrom(), dto.getText(), LocalDateTime.parse(dto.getSendingTime())));
            log.info("Sms üretildi ve kaydedildi.");
        }
    }
}
