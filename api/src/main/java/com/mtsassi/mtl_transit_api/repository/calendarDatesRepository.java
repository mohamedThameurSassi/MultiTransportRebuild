package com.mtsassi.mtl_transit_api.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mtsassi.mtl_transit_api.model.entity.CalendarDatesModel;
import org.springframework.stereotype.Repository;


@Repository
public interface CalendarDatesRepository extends JpaRepository<CalendarDatesModel, Long> {
    List<CalendarDatesModel> findByServiceId(String serviceId);
}
