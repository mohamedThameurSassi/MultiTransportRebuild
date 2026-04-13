package com.mtsassi.mtl_transit_api.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
/*/
CREATE TABLE IF NOT EXISTS calendar (
    service_id  TEXT PRIMARY KEY,
    monday      BOOLEAN NOT NULL,
    tuesday     BOOLEAN NOT NULL,
    wednesday   BOOLEAN NOT NULL,
    thursday    BOOLEAN NOT NULL,
    friday      BOOLEAN NOT NULL,
    saturday    BOOLEAN NOT NULL,
    sunday      BOOLEAN NOT NULL,
    start_date  DATE NOT NULL,
    end_date    DATE NOT NULL
);


*/

@Entity
@Table(name = "calendar")
@NoArgsConstructor
@Getter
@Setter
public class Calendar {
    @Id
    @Column(name = "service_id")
    private String serviceId;

    @Column(name = "monday", nullable = false)
    private Boolean monday;

    @Column(name = "tuesday", nullable = false)
    private Boolean tuesday;

    @Column(name = "wednesday", nullable = false)
    private Boolean wednesday;

    @Column(name = "thursday", nullable = false)
    private Boolean thursday;

    @Column(name = "friday", nullable = false)
    private Boolean friday;

    @Column(name = "saturday", nullable = false)
    private Boolean saturday;

    @Column(name = "sunday", nullable = false)
    private Boolean sunday;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
