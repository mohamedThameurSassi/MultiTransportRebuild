package com.mtsassi.mtl_transit_api.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/*

CREATE TABLE IF NOT EXISTS calendar_dates (
    service_id      TEXT REFERENCES calendar(service_id),
    date            DATE NOT NULL,
    exception_type  INTEGER NOT NULL,  -- 1=added, 2=removed
    PRIMARY KEY (service_id, date)
); */
@Entity
@Table(name = "calendar_dates")
@Getter
@Setter
public class CalendarDatesModel {
    @Id
    @Column(name = "service_id")
    private String serviceId;
    @Column(name = "date", nullable = false)
    private String date;
    @Column(name = "exception_type", nullable = false)
    private Integer exceptionType;
}
