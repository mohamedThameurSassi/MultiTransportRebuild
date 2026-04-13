package com.mtsassi.mtl_transit_api.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mtsassi.mtl_transit_api.model.entity.StopTimesModel;
import org.springframework.stereotype.Repository;
@Repository
public interface StopsTimesRepository extends JpaRepository<StopTimesModel, Long> {
    List<StopTimesModel> findByTripId(String tripId);  
    
}
