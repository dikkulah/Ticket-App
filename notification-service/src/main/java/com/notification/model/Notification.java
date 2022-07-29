package com.notification.model;


import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.io.Serializable;


@Getter
@Setter
public class Notification implements Serializable {

    @Id
    private ObjectId id = ObjectId.get();
    private String text;

    public Notification() {
    }

    public Notification(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
