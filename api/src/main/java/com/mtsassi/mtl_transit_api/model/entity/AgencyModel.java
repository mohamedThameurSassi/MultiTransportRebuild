package com.mtsassi.mtl_transit_api.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;




@Entity
@Table(name = "agencies")
@Getter
@Setter
public class AgencyModel {
    @Id
    private String agencyId;

    @Column(nullable = false)
    private String name;
    @Column
    private String url;
    
    @Column(nullable = false)
    private String timezone = "America/Montreal";
}
