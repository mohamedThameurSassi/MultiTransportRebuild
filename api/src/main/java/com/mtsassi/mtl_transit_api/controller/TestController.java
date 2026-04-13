package com.mtsassi.mtl_transit_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import java.util.Map;

import com.mtsassi.mtl_transit_api.repository.AgencyRepository;
import com.mtsassi.mtl_transit_api.repository.CalendarRepository;
import com.mtsassi.mtl_transit_api.repository.RoutesRepository;
import com.mtsassi.mtl_transit_api.repository.ShapesRepository;
import com.mtsassi.mtl_transit_api.repository.StopsRepository;
import com.mtsassi.mtl_transit_api.repository.StopsTimesRepository;
import com.mtsassi.mtl_transit_api.repository.TransfersRepository;
import com.mtsassi.mtl_transit_api.repository.TripsRepository;
import com.mtsassi.mtl_transit_api.repository.calendarDatesRepository;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final AgencyRepository agencyRepository;
    private final CalendarRepository calendarRepository;
    private final RoutesRepository routesRepository;
    private final ShapesRepository shapesRepository;
    private final StopsRepository stopsRepository;
    private final StopsTimesRepository stopsTimesRepository;
    private final TransfersRepository transfersRepository;
    private final TripsRepository tripsRepository;
    private final calendarDatesRepository calendarDatesRepo;

    @GetMapping("/counts")
    public Map<String, Long> getCounts() {
        return Map.of(
                "agencies", agencyRepository.count(),
                "calendars", calendarRepository.count(),
                "routes", routesRepository.count(),
                "shapes", shapesRepository.count(),
                "stops", stopsRepository.count(),
                "stopTimes", stopsTimesRepository.count(),
                "transfers", transfersRepository.count(),
                "trips", tripsRepository.count(),
                "calendarDates", calendarDatesRepo.count()
        );
    }
}
