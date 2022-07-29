package com.notification.repository;

import com.notification.model.Sms;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SmsRepository extends MongoRepository<Sms,String> {
}
