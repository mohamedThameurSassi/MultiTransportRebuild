package com.mtsassi.mtl_transit_api.model.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



/*
CREATE TABLE IF NOT EXISTS trips (
    trip_id         TEXT PRIMARY KEY,
    route_id        TEXT NOT NULL REFERENCES routes(route_id),
    service_id      TEXT NOT NULL REFERENCES calendar(service_id),
    trip_headsign   TEXT,
    direction_id    INTEGER,
    shape_id        TEXT
);
*/


@Entity
@Table(name = "trips")
@NoArgsConstructor
@Getter
@Setter
public class TripsModel {
    @Id
    @Column(name = "trip_id")
    private String tripId;

    @Column(name = "route_id", nullable = false)
    private String routeId;
    @Column(name = "service_id", nullable = false)
    private String serviceId;
    @Column(name = "trip_headsign")
    private String tripHeadsign;
    @Column(name = "direction_id")
    private Integer directionId;
    @Column(name = "shape_id")
    private String shapeId;
}
