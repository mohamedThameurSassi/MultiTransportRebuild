package com.mtsassi.mtl_transit_api.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/*

CREATE TABLE IF NOT EXISTS shapes (
    shape_id            TEXT NOT NULL,
    shape_pt_lat        DOUBLE PRECISION NOT NULL,
    shape_pt_lon        DOUBLE PRECISION NOT NULL,
    shape_pt_sequence   INTEGER NOT NULL,
    PRIMARY KEY (shape_id, shape_pt_sequence)
);

*/
@Entity
@Table(name = "shapes")
@Getter
@Setter
public class ShapesModel {
    @Id
    @Column(name = "shape_id")
    private String shapeId;
    @Column(name = "shape_pt_lat", nullable = false)
    private Double shapePtLat;
    @Column(name = "shape_pt_lon", nullable = false)
    private Double shapePtLon;
    @Column(name = "shape_pt_sequence", nullable = false)
    private Integer shapePtSequence;
}
