package com.mtsassi.mtl_transit_api.routing.raptor.service;

import com.mtsassi.mtl_transit_api.routing.raptor.model.JourneyPointer;
import com.mtsassi.mtl_transit_api.routing.raptor.model.RaptorRoute;
import com.mtsassi.mtl_transit_api.routing.raptor.model.RaptorStop;
import com.mtsassi.mtl_transit_api.routing.raptor.model.RaptorTransfer;
import com.mtsassi.mtl_transit_api.routing.raptor.model.RaptorTrip;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RaptorRouter {

    private final TransitGraphStore graphStore;
    private static final int MAX_ROUNDS = 5;
    private static final int INF = Integer.MAX_VALUE;

    public List<JourneyPointer> route(String originStopId, String destStopId, int departureTimeSeconds) {
        // State initialization
        // NOTE: This is a single-criteria RAPTOR implementation (Earliest Arrival Time).
        // For production, you may want to extend this to multi-criteria (time vs fewest transfers)
        // to provide pareto-optimal alternatives.
        Map<String, Integer> earliestArrival = new HashMap<>();
        Map<String, JourneyPointer> pointers = new HashMap<>(); // Simplified for single-criteria
        Set<String> markedStops = new HashSet<>();

        // Initialize all known stops to infinity
        for (String stopId : graphStore.getStops().keySet()) {
            earliestArrival.put(stopId, INF);
        }

        earliestArrival.put(originStopId, departureTimeSeconds);
        markedStops.add(originStopId);

        for (int k = 1; k <= MAX_ROUNDS; k++) {
            if (markedStops.isEmpty()) break;

            // Step A: Accumulate routes
            Map<String, String> activeRoutes = new HashMap<>(); // RouteId -> BoardingStopId
            for (String stopId : markedStops) {
                RaptorStop stop = graphStore.getStops().get(stopId);
                if (stop == null || stop.getRouteIds() == null) continue;
                
                for (String routeId : stop.getRouteIds()) {
                    RaptorRoute route = graphStore.getRoutes().get(routeId);
                    // Keep the earliest boarding stop in the sequence
                    if (!activeRoutes.containsKey(routeId)) {
                        activeRoutes.put(routeId, stopId);
                    } else {
                        int currentIndex = route.getStopIds().indexOf(stopId);
                        int existingIndex = route.getStopIds().indexOf(activeRoutes.get(routeId));
                        if (currentIndex < existingIndex) {
                            activeRoutes.put(routeId, stopId);
                        }
                    }
                }
            }

            markedStops.clear();

            // Step B: Traverse routes
            for (Map.Entry<String, String> entry : activeRoutes.entrySet()) {
                RaptorRoute route = graphStore.getRoutes().get(entry.getKey());
                String boardingStopId = entry.getValue();
                int boardingIndex = route.getStopIds().indexOf(boardingStopId);
                
                RaptorTrip activeTrip = null;
                String currentBoardingStop = boardingStopId;

                for (int i = boardingIndex; i < route.getStopIds().size(); i++) {
                    String currentStopId = route.getStopIds().get(i);

                    // If we are on a trip, check if we improve the arrival time
                    if (activeTrip != null) {
                        // Safety check: Our TransitGraphLoader guarantees that trip StopTimes map 1:1 to route StopIds 
                        if (i >= activeTrip.getStopTimes().size()) break;
                        
                        int arrivalTime = activeTrip.getStopTimes().get(i).getArrivalTimeSeconds();
                        if (arrivalTime < earliestArrival.get(currentStopId)) {
                            earliestArrival.put(currentStopId, arrivalTime);
                            markedStops.add(currentStopId);
                            pointers.put(currentStopId, new JourneyPointer(currentBoardingStop, route.getPatternId(), activeTrip.getTripId(), false));
                        }
                    }

                    // Try to catch an earlier trip at the current stop
                    if (activeTrip == null || earliestArrival.get(currentStopId) < activeTrip.getStopTimes().get(i).getDepartureTimeSeconds()) {
                        int currentStopArrivalTime = earliestArrival.get(currentStopId);
                        for (RaptorTrip trip : route.getTrips()) {
                            if (i < trip.getStopTimes().size() && trip.getStopTimes().get(i).getDepartureTimeSeconds() >= currentStopArrivalTime) {
                                activeTrip = trip;
                                currentBoardingStop = currentStopId;
                                break;
                            }
                        }
                    }
                }
            }

            // Step C: Process Footpaths (Transfers)
            Set<String> newlyMarked = new HashSet<>(markedStops);
            for (String stopId : newlyMarked) {
                RaptorStop stop = graphStore.getStops().get(stopId);
                if (stop == null || stop.getTransfers() == null) continue;

                int arrivalAtStop = earliestArrival.get(stopId);
                for (RaptorTransfer transfer : stop.getTransfers()) {
                    int walkArrivalTime = arrivalAtStop + transfer.getWalkTime();
                    String destId = transfer.getDestinationId();
                    
                    if (walkArrivalTime < earliestArrival.get(destId)) {
                        earliestArrival.put(destId, walkArrivalTime);
                        markedStops.add(destId);
                        pointers.put(destId, new JourneyPointer(stopId, null, null, true));
                    }
                }
            }
        }

        return reconstructJourney(destStopId, originStopId, pointers);
    }

    private List<JourneyPointer> reconstructJourney(String destStopId, String originStopId, Map<String, JourneyPointer> pointers) {
        List<JourneyPointer> journey = new ArrayList<>();
        String current = destStopId;
        
        while (current != null && !current.equals(originStopId)) {
            JourneyPointer ptr = pointers.get(current);
            if (ptr == null) break; // Path not found
            journey.add(ptr);
            current = ptr.getPreviousStopId();
        }
        Collections.reverse(journey);
        return journey;
    }
}