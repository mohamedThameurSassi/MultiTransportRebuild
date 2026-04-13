package com.mtsassi.mtl_transit_api.routing.raptor.model;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RaptorTrip {
    private String tripId;
    private List<StopTime> stopTimes;
    
    @Getter
    @Setter
    public static class StopTime{
    
    private int arrivalTimeSeconds;
    private int departureTimeSeconds;
        
    }
}
