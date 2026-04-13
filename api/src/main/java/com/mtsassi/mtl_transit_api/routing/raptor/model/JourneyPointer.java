package com.mtsassi.mtl_transit_api.routing.raptor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JourneyPointer {
    private String previousStopId;
    private String patternId;
    private String tripId;
    private boolean isTransfer;
}