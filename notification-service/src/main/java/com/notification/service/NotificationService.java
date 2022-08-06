package com.notification.service;

import com.notification.dto.NotificationDto;

public interface NotificationService {
    void send(NotificationDto dto);
}
