package com.ticket.dto;

import java.io.Serializable;


public class MailDto extends NotificationDto implements Serializable {
    private String title;
    private String to;
    private String from;
    private String sendingTime;

    public MailDto(String text, String title, String to, String from, String sendingTime) {
        super(text);
        this.title = title;
        this.to = to;
        this.from = from;
        this.sendingTime = sendingTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(String sendingTime) {
        this.sendingTime = sendingTime;
    }
}
