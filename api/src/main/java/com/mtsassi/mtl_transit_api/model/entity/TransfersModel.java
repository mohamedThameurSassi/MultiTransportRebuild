package com.mtsassi.mtl_transit_api.model.entity;

import org.hibernate.annotations.Collate;

import jakarta.persistence.IdClass;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;
/*CREATE TABLE IF NOT EXISTS transfers (
    from_stop_id        TEXT NOT NULL REFERENCES stops(stop_id),
    to_stop_id          TEXT NOT NULL REFERENCES stops(stop_id),
    transfer_type       INTEGER NOT NULL,  -- 0=recommended, 1=timed, 2=min_time, 3=not_possible
    min_transfer_time   INTEGER,           -- seconds
    PRIMARY KEY (from_stop_id, to_stop_id)
); */

@Entity
@Table(name = "transfers")
@IdClass(TransfersModel.TransfersId.class)
@NoArgsConstructor
@Getter
@Setter
public class TransfersModel {
    @Id
    @Column(name = "from_stop_id")
    private String fromStopId;

    @Id
    @Column(name = "to_stop_id", nullable = false)
    private String toStopId;
    @Column(name = "transfer_type", nullable = false)
    private Integer transferType;
    @Column(name = "min_transfer_time")
    private Integer minTransferTime;
    
    @lombok.Data
    public static class TransfersId implements java.io.Serializable {
        private String fromStopId;
        private String toStopId;
    }
}
