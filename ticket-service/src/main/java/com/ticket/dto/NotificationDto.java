package com.ticket.dto;

import java.io.Serializable;

public class NotificationDto implements Serializable {
    private String text;

    public NotificationDto(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
