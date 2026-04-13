package com.mtsassi.mtl_transit_api.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

/*
CREATE TABLE IF NOT EXISTS stops (
    stop_id         TEXT PRIMARY KEY,
    stop_code       TEXT,
    stop_name       TEXT NOT NULL,
    location        GEOMETRY(Point, 4326) NOT NULL,
    location_type   INTEGER DEFAULT 0,  -- 0=stop, 1=station
    parent_station  TEXT DEFAULT 0,
    wheelchair      INTEGER DEFAULT 0
);
*/

@Entity
@Table(name = "stops")
@NoArgsConstructor
@Getter
@Setter
public class StopsModel {
    @Id
    @Column(name = "stop_id")
    private String stopId;

    @Column(name = "stop_code")
    private String stopCode;

    @Column(name = "stop_name", nullable = false)
    private String stopName;

    @Column(name = "location", nullable = false)
    private Point location;

    @Column(name = "location_type", nullable = false)
    private Integer locationType;

    @Column(name = "parent_station")
    private String parentStation;

    @Column(name = "wheelchair", nullable = false)
    private Integer wheelchair;
}
