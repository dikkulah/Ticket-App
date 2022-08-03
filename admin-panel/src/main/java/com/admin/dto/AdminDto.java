package com.admin.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Getter
@Setter
public class AdminDto implements Serializable {
    private Long id;
    @Email(message = "Girilen değer bir emaile benzemiyor.")
    private String email;
    @Size(min = 5, message = "Şifre minimum 5 karakter olmalı.")
    private String password;
}
