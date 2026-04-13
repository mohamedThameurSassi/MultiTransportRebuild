package com.mtsassi.mtl_transit_api.routing.raptor.service;
import com.mtsassi.mtl_transit_api.routing.raptor.model.RaptorRoute;
import com.mtsassi.mtl_transit_api.routing.raptor.model.RaptorStop;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
@Component
@Getter
public class TransitGraphStore {
    private final Map<String, RaptorStop> stops = new HashMap<>();
    private final Map<String, RaptorRoute> routes = new HashMap<>();
}
