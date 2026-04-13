package com.mtsassi.mtl_transit_api.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mtsassi.mtl_transit_api.model.entity.AgencyModel;
import java.util.List;
import java.util.Optional;


@Repository
public interface AgencyRepository extends JpaRepository<AgencyModel, String> {
    List<AgencyModel> findByName(String name);
    Optional<AgencyModel> findById(String id);
}
