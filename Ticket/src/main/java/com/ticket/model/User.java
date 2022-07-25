package com.ticket.model;

import com.ticket.model.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Email(message = "Girilen değer bir emaile benzemiyor.")
    private String email;
    @Column(nullable = false,length = 25)
    @Size(min = 5,message = "Şifre minimum 5 karakter olmalı.")
    private String password;
    private UserType userType;



    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets;


}
