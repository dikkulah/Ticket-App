package com.admin.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Integer seatNo;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false, updatable = false)
    private Trip trip;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false, updatable = false)
    private Passenger passenger;


}
