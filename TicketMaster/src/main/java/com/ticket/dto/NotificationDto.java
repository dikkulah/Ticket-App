package com.ticket.dto;

import com.ticket.dto.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.AmqpTemplate;

import java.io.Serializable;

@Getter
@Setter
@Builder
public  class NotificationDto implements Serializable {
    private NotificationType type;
    private String title;
    private String to;
    private String from;
    private String text;
    private String sendingTime;


    public NotificationDto(NotificationType type, String title, String to, String from, String text, String sendingTime) {
        this.type = type;
        this.title = title;
        this.to = to;
        this.from = from;
        this.text = text;
        this.sendingTime = sendingTime;
    }
}
