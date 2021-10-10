package com.example.sensorApp.repository;

import com.example.sensorApp.domain.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Vishal Maral
 * Date: 10/7/21
 */
@Repository
public interface StatusRepository extends JpaRepository<Status,String> {
    Optional<Status> findBySensorId(String sensorId);
}
