package com.mtsassi.mtl_transit_api.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mtsassi.mtl_transit_api.model.entity.ShapesModel;
import org.springframework.stereotype.Repository;
@Repository

public interface ShapesRepository extends JpaRepository<ShapesModel, Long> {
    List<ShapesModel> findByShapeId(String shapeId);
    
}
