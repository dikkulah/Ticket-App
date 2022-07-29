package com.ticket.dto;

import java.io.Serializable;

public class SmsDto extends NotificationDto implements Serializable {
    private String phoneNumber;


    public SmsDto(String text, String phoneNumber) {
        super(text);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
