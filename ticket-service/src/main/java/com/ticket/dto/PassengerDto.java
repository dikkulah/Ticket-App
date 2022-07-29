package com.ticket.dto;

import com.ticket.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDto implements Serializable {
    private Long id;
    private Gender gender;
    private String fullName;
}
