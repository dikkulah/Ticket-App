package com.ticket.dto;

import com.ticket.model.enums.Gender;
import lombok.Data;

import java.io.Serializable;

@Data
public class PassengerDto implements Serializable {
    private Long id;
    private String fullName;
    private String tcNo;
    private String phoneNumber;
    private Gender gender;
}
