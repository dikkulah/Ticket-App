package com.admin.dto;

import com.admin.dto.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
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
