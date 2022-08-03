package com.notification.listener;


import com.notification.dto.NotificationDto;
import com.notification.model.NotificationFactory;
import com.notification.repository.MailRepository;
import com.notification.repository.SmsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j

public class NotificationListener {

    @Autowired
    private SmsRepository smsRepository;
    @Autowired
    private MailRepository mailRepository;

    @RabbitListener(queues = "ticketMaster")
    public void handleNotification(NotificationDto dto) {
        NotificationFactory factory = new NotificationFactory();
        factory.produceNotification(dto,smsRepository,mailRepository);

    }
}
