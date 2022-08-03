package com.ticket.repository;

import com.ticket.model.Trip;
import com.ticket.model.enums.Station;
import com.ticket.model.enums.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("select t from Trip t where (cast(:arrivalTime as timestamp) is null or t.arrivalTime= :arrivalTime)" +
            "and (cast(:departureTime as timestamp) is null or t.departureTime= :departureTime)" +
            "and (:vehicle is null or t.vehicle= :vehicle)" +
            "and (:toStation is null or t.toStation= :toStation)" +
            "and (:fromStation is null or t.fromStation= :fromStation)"
            + "and t.isCanceled=false")
    List<Trip> findActiveTripsByProperties(
            @Param("arrivalTime") LocalDateTime arrivalTime,
            @Param("departureTime") LocalDateTime departureTime,
            @Param("vehicle") Vehicle vehicle,
            @Param("toStation") Station to,
            @Param("fromStation") Station from);


}
