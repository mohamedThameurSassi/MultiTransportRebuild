package com.mtsassi.mtl_transit_api.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mtsassi.mtl_transit_api.routing.raptor.model.JourneyPointer;
import com.mtsassi.mtl_transit_api.routing.raptor.model.RaptorRoute;
import com.mtsassi.mtl_transit_api.routing.raptor.service.RaptorRouter;
import com.mtsassi.mtl_transit_api.routing.raptor.service.TransitGraphStore;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/routing")
@RequiredArgsConstructor
public class RoutingController {

    private final TransitGraphStore graphStore;
    private final RaptorRouter raptorRouter;

    @GetMapping("/routes")
    public List<RaptorRoute> getRoutes() {
        return new ArrayList<>(graphStore.getRoutes().values());
    }

    @GetMapping("/journey")
    public List<JourneyPointer> getJourney(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(defaultValue = "08:00:00") String time) {
        
        int departureTimeSeconds = LocalTime.parse(time).toSecondOfDay();
        
        return raptorRouter.route(origin, destination, departureTimeSeconds);
    }
}
