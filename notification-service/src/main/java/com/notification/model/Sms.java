package com.notification.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(value = "sms")
@Getter
@Setter
public class Sms extends Notification implements Serializable {
    private String phoneNumber;

    public Sms() {
    }

    public Sms(String text, String phoneNumber) {
        super(text);
        this.phoneNumber = phoneNumber;
    }

}


