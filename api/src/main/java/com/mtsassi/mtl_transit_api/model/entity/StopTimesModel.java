package com.mtsassi.mtl_transit_api.model.entity;

import org.hibernate.annotations.Collate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/*
    
CREATE TABLE IF NOT EXISTS stop_times (
    trip_id             TEXT NOT NULL REFERENCES trips(trip_id),
    arrival_time        INTERVAL NOT NULL,
    departure_time      INTERVAL NOT NULL,
    stop_id             TEXT NOT NULL REFERENCES stops(stop_id),
    stop_sequence       INTEGER NOT NULL,
    pickup_type         INTEGER DEFAULT 0,
    PRIMARY KEY (trip_id, stop_sequence)
); */
@Entity
@Table(name = "stop_times")
@Getter
@Setter
public class StopTimesModel {
    @Id
    @Column(name = "trip_id")
    private String tripId;
    @Column(name = "arrival_time", nullable = false)
    private String arrivalTime;
    @Column(name = "departure_time", nullable = false)
    private String departureTime;
    @Column(name = "stop_id", nullable = false)
    private String stopId;
    @Column(name = "stop_sequence", nullable = false)
    private Integer stopSequence;
    @Column(name = "pickup_type")
    private Integer pickupType;
}
