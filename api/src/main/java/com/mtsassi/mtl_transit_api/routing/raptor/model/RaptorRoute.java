package com.mtsassi.mtl_transit_api.routing.raptor.model;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import com.mtsassi.mtl_transit_api.routing.raptor.model.RaptorTrip;

@Getter
@Setter
public class RaptorRoute {
    private String patternId;
    private List<String> stopIds;
    private List<RaptorTrip> trips;
}
