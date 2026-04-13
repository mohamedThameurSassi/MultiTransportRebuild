package com.mtsassi.mtl_transit_api.routing.raptor.model;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RaptorStop {
    private String stopId;
    private List<RaptorTransfer> transfers;
    private List<String> routeIds;
}
