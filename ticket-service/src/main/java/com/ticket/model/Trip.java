package com.ticket.model;

import com.ticket.model.enums.Station;
import com.ticket.model.enums.Vehicle;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
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
    @Column(length = 5,nullable = false)
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Varış noktası boş bırakılamaz")
    private Station fromStation;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Bitiş noktası boş bırakılamaz")
    private Station toStation;

    @Column(updatable = false)
    private Integer seatCapacity;

    @ElementCollection
    private Set<Integer> purchasedSeats = new HashSet<>();

    @NotNull(message = "Kalkış zamanı boş bıraklamaz")
    private LocalDateTime departureTime;
    @NotNull(message = "Varış zamanı boş bıraklamaz")
    private LocalDateTime arrivalTime;

    @OneToMany(mappedBy = "trip")
    private List<Ticket> tickets;

    private Boolean isCanceled = false;



}
