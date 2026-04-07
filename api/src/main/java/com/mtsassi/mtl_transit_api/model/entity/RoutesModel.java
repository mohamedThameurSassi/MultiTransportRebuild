package com.mtsassi.mtl_transit_api.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/*
CREATE TABLE IF NOT EXISTS routes (
    route_id    TEXT PRIMARY KEY,
    agency_id   TEXT REFERENCES agencies(agency_id),
    short_name  TEXT,
    long_name   TEXT,
    route_type  INTEGER NOT NULL  -- 0=tram, 1=metro, 2=rail, 3=bus
);

*/

@Entity
@Table(name = "routes")
@Getter
@Setter
public class RoutesModel {
    @Id
    @Column(name = "route_id")
    private String routeId;

    @Column(name = "agency_id")
    private String agencyId;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "long_name")
    private String longName;

    @Column(name = "route_type", nullable = false)
    private int routeType;
}

