package com.mtsassi.mtl_transit_api.routing.raptor.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.mtsassi.mtl_transit_api.repository.RoutesRepository;
import com.mtsassi.mtl_transit_api.repository.StopsRepository;
import com.mtsassi.mtl_transit_api.repository.TransfersRepository;
import com.mtsassi.mtl_transit_api.repository.TripsRepository;
import com.mtsassi.mtl_transit_api.repository.StopsTimesRepository;
import com.mtsassi.mtl_transit_api.model.entity.StopTimesModel;
import com.mtsassi.mtl_transit_api.model.entity.TripsModel;
import com.mtsassi.mtl_transit_api.routing.raptor.model.RaptorStop;
import com.mtsassi.mtl_transit_api.routing.raptor.model.RaptorTransfer;
import com.mtsassi.mtl_transit_api.routing.raptor.model.RaptorRoute;
import com.mtsassi.mtl_transit_api.routing.raptor.model.RaptorTrip;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransitGraphLoader {
    private final TransitGraphStore graphStore;
    private final StopsRepository stopsRepository;
    private final RoutesRepository routesRepository;
    private final TransfersRepository transfersRepository;
    private final TripsRepository tripsRepository;
    private final StopsTimesRepository stopTimesRepository;

    @PostConstruct
    public void loadGraph(){
        System.out.println("Starting to load transit graph into memory...");
        
        stopsRepository.findAll().forEach(stopModel -> {
            RaptorStop raptorStop  = new RaptorStop();
            raptorStop.setStopId(stopModel.getStopId());
            raptorStop.setTransfers(new ArrayList<>());
            raptorStop.setRouteIds(new ArrayList<>());
            graphStore.getStops().put(raptorStop.getStopId(), raptorStop);
        });

        transfersRepository.findAll().forEach(transferModel -> {
            RaptorTransfer raptorTransfer = new RaptorTransfer();
            raptorTransfer.setDestinationId(transferModel.getToStopId());
            raptorTransfer.setWalkTime(transferModel.getMinTransferTime() != null ? transferModel.getMinTransferTime() : 0);
            RaptorStop originStop = graphStore.getStops().get(transferModel.getFromStopId());
            if (originStop != null) {
                originStop.getTransfers().add(raptorTransfer);
            }
        });

        System.out.println("Loading StopTimes in batches...");
        Map<String, List<StopTimesModel>> stopTimesByTrip = new HashMap<>();
        int page = 0;
        int size = 250000;
        Page<StopTimesModel> stopTimesPage;
        
        do {
            stopTimesPage = stopTimesRepository.findAll(PageRequest.of(page, size));
            for (StopTimesModel stm : stopTimesPage.getContent()) {
                stopTimesByTrip.computeIfAbsent(stm.getTripId(), k -> new ArrayList<>()).add(stm);
            }
            page++;
            if (page % 4 == 0) {
                System.out.println("Loaded " + (page * size) + " StopTimes...");
            }
        } while (stopTimesPage.hasNext());

        stopTimesByTrip.values().forEach(list -> 
            list.sort(Comparator.comparingInt(StopTimesModel::getStopSequence))
        );

        System.out.println("Processing Trips and generating RaptorRoutes (Patterns)...");
        List<TripsModel> allTrips = tripsRepository.findAll();
        
        Map<String, RaptorRoute> raptorRoutes = new HashMap<>();

        for (TripsModel tripModel : allTrips) {
            String tripId = tripModel.getTripId();
            List<StopTimesModel> tripStopTimes = stopTimesByTrip.get(tripId);
            
            if (tripStopTimes == null || tripStopTimes.isEmpty()) {
                continue;
            }

            List<String> stopSequence = tripStopTimes.stream()
                    .map(StopTimesModel::getStopId)
                    .collect(Collectors.toList());

            String routePatternId = tripModel.getRouteId() + "_" + stopSequence.hashCode();

            RaptorRoute raptorRoute = raptorRoutes.computeIfAbsent(routePatternId, k -> {
                RaptorRoute newRoute = new RaptorRoute();
                newRoute.setPatternId(routePatternId);
                newRoute.setStopIds(stopSequence);
                newRoute.setTrips(new ArrayList<>());
                for (String stopId : stopSequence) {
                    RaptorStop stop = graphStore.getStops().get(stopId);
                    if (stop != null) {
                        if (!stop.getRouteIds().contains(routePatternId)) {
                            stop.getRouteIds().add(routePatternId);
                        }
                    }
                }
                
                return newRoute;
            });

            RaptorTrip raptorTrip = new RaptorTrip();
            raptorTrip.setTripId(tripId);
            List<RaptorTrip.StopTime> raptorStopTimes = new ArrayList<>();
            
            for (StopTimesModel stm : tripStopTimes) {
                RaptorTrip.StopTime st = new RaptorTrip.StopTime();
                st.setArrivalTimeSeconds((int) stm.getArrivalTime().getSeconds());
                st.setDepartureTimeSeconds((int) stm.getDepartureTime().getSeconds());
                raptorStopTimes.add(st);
            }
            
            raptorTrip.setStopTimes(raptorStopTimes);
            raptorRoute.getTrips().add(raptorTrip);
        }

        raptorRoutes.values().forEach(route -> {
            route.getTrips().sort(Comparator.comparingInt(t -> t.getStopTimes().get(0).getDepartureTimeSeconds()));
            graphStore.getRoutes().put(route.getPatternId(), route);
        });

        System.out.println("Transit Graph completely loaded! " + 
            graphStore.getStops().size() + " stops and " + 
            graphStore.getRoutes().size() + " route variants mapped.");
    }
}
