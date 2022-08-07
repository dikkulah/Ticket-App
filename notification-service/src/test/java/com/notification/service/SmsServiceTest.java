package com.notification.service;

import com.notification.dto.NotificationDto;
import com.notification.model.Sms;
import com.notification.model.enums.NotificationType;
import com.notification.repository.SmsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
@SpringBootTest
class SmsServiceTest {

    @InjectMocks
    SmsService  smsService;
    @Mock
    SmsRepository smsRepository;

    @Test
    void send() {

        var sms = new Sms();
        var a= LocalDateTime.now();
        sms.setSendingTime(a);
        when(smsRepository.save(sms)).thenReturn(sms);
        smsService.send(new NotificationDto(NotificationType.SMS,null,null,null,null, a.toString()));
        verify(smsRepository,times(1)).save(sms);
    }
}