package com.mtsassi.mtl_transit_api.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mtsassi.mtl_transit_api.model.entity.RoutesModel;
import org.springframework.stereotype.Repository;
@Repository
public interface RoutesRepository extends JpaRepository<RoutesModel, Long> {
    List<RoutesModel> findByRouteId(String routeId);
}
