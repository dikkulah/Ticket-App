package com.ticket.repository;

import com.ticket.model.Trip;
import com.ticket.model.enums.Station;
import com.ticket.model.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("select t from Trip t where (:arrivalTime is null or t.arrivalTime= :arrivalTime)" +
            "and (:departureTime is null or t.departureTime= :departureTime)" +
            "and (:vehicleType is null or t.vehicleType= :vehicleType)" +
            "and (:to is null or t.toStation= :to)" +
            "and (:from is null or t.fromStation= :from)")
    List<Trip> findTripsByProperties(@Param("arrivalTime") LocalDateTime arrivalTime
            , @Param("departureTime") LocalDateTime departureTime
            , @Param("vehicleType") VehicleType vehicleType
            , @Param("to") Station to
            , @Param("from") Station from);



}
