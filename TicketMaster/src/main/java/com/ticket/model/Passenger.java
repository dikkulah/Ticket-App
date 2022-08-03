package com.ticket.model;

import com.ticket.model.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Passenger {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String tcNo;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String phoneNumber;
    @OneToMany(mappedBy = "passenger")
    private List<Ticket> ticket;


}
