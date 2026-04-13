package com.mtsassi.mtl_transit_api.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mtsassi.mtl_transit_api.model.entity.TransfersModel;
import org.springframework.stereotype.Repository;
@Repository
public interface TransfersRepository extends JpaRepository<TransfersModel, Long> {
    List<TransfersModel> findByFromStopId(String fromStopId);
    List<TransfersModel> findByToStopId(String toStopId);
    
}
