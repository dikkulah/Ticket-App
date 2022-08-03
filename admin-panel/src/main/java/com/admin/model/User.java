package com.admin.model;


import com.admin.model.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
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

    @Column(length = 11)
    private String phoneNumber;

    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private UserType userType;


}
