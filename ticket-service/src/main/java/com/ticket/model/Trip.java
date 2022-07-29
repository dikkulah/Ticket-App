package com.ticket.model;

import com.ticket.model.enums.Station;
import com.ticket.model.enums.VehicleType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 5)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    private Station fromStation;

    @Enumerated(EnumType.STRING)
    private Station toStation;

    @Column(updatable = false)
    private Integer seatCapacity;

    @ElementCollection
    private Set<Integer> purchasedSeats ;

    @NotEmpty
    private LocalDateTime departureTime;

    @NotEmpty
    private LocalDateTime arrivalTime;

    @OneToMany(mappedBy = "trip")
    private List<Ticket> tickets;

}
