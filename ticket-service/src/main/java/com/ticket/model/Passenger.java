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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Gender gender;
    private String fullName;
    @OneToMany(mappedBy = "passenger")
    private List<Ticket> ticket;
}
