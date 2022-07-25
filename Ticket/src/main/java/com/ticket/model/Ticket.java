package com.ticket.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false, updatable = false)
    private Payment payment;
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false, updatable = false)
    private Trip trip;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;


}
