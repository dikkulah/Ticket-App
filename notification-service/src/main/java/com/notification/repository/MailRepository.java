package com.notification.repository;

import com.notification.model.Mail;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MailRepository extends MongoRepository<Mail,String> {
}
