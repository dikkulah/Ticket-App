package com.notification.service;

import com.notification.dto.NotificationDto;
import com.notification.model.Mail;
import com.notification.model.enums.NotificationType;
import com.notification.repository.MailRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;


@SpringBootTest
class MailServiceTest {
    @InjectMocks
    MailService mailService;
    @Mock
    MailRepository mailRepository;

    @Test
    void send() {
        var mail = new Mail();
        var a= LocalDateTime.now();
        mail.setSendingTime(a);
        when(mailRepository.save(mail)).thenReturn(mail);
        mailService.send(new NotificationDto(NotificationType.EMAIL,null,null,null,null, a.toString()));
        verify(mailRepository,times(1)).save(mail);



    }
}