package com.ticket.dto;

import com.ticket.model.enums.Role;
import com.ticket.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    @Email(message = "Girilen değer bir emaile benzemiyor.")
    private String email;
    @Size(min = 5, message = "Şifre minimum 5 karakter olmalı.")
    private String password;
    private String phoneNumber;
    private List<TicketDto> tickets;
    private UserType userType;
    private Role role;

}
