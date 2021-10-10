package com.example.sensorApp.repository;

import com.example.sensorApp.domain.entity.Measurement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Vishal Maral
 * Date: 10/7/21
 */
@Repository
public interface MeasurementRepository extends JpaRepository<Measurement,String> {
    List<Measurement> findBySensorIdOrderByTimeDesc(String sensorId, Pageable pageable);
}
