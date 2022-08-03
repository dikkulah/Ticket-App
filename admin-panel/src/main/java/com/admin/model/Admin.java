package com.admin.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Email(message = "Girilen değer bir emaile benzemiyor.")
    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    @Size(min = 5,message = "Şifre minimum 5 karakter olmalı.")
    private String password;
}
