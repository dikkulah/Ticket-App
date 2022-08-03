package com.admin.model;

import com.admin.model.enums.Station;
import com.admin.model.enums.Vehicle;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Column(length = 5, nullable = false)
    private Vehicle vehicle;
    private BigDecimal ticketPrice;


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
    private List<Ticket> tickets = new ArrayList<>();

    private Boolean isCanceled = false;


}
