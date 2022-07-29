package com.notification.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "mails")
@Getter
@Setter
public class Mail extends Notification implements Serializable {
    private String title;
    private String to;
    private String from;
    private String sendingTime;



    public Mail() {
    }

    public Mail(String text, String title, String to, String from, String sendingTime) {
        super(text);
        this.title = title;
        this.to = to;
        this.from = from;
        this.sendingTime = sendingTime;
    }
}
