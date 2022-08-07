package com.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("mails")
@AllArgsConstructor@Slf4j
@Data
public class Mail  {
    @Id
    private String id;
    private String title;
    private String to;
    private String from;
    private String text;
    private LocalDateTime sendingTime;

    public Mail(String title, String to, String from, String text, LocalDateTime sendingTime) {
        this.title = title;
        this.to = to;
        this.from = from;
        this.text = text;
        this.sendingTime = sendingTime;
    }


    public Mail() {

    }
}
