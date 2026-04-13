package com.mtsassi.mtl_transit_api.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mtsassi.mtl_transit_api.model.entity.StopsModel;
import org.springframework.stereotype.Repository;
@Repository
public interface StopsRepository extends JpaRepository<StopsModel, Long> {
    List<StopsModel> findByStopId(String stopId);
    
}
