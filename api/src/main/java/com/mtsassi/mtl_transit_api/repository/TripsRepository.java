package com.mtsassi.mtl_transit_api.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mtsassi.mtl_transit_api.model.entity.TripsModel;
import org.springframework.stereotype.Repository;
@Repository
public interface TripsRepository extends JpaRepository<TripsModel, Long> {
    List<TripsModel> findByTripId(String tripId);
    
}
