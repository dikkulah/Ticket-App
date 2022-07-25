package com.ticket.model;

import com.ticket.model.enums.VehicleType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Getter
@Setter
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    @NotEmpty
    private String from;
    @NotEmpty
    private String to;


    private Integer seats;


    @OneToMany(mappedBy = "trip")
    private List<Ticket> tickets;

}
