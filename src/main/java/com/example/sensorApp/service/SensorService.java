package com.example.sensorApp.service;

import com.example.sensorApp.domain.dto.MeasurementDto;
import com.example.sensorApp.domain.entity.Measurement;
import com.example.sensorApp.repository.MeasurementRepository;
import org.springframework.stereotype.Service;

/**
 * @author Vishal Maral
 * Date: 10/7/21
 */
@Service
public class SensorService {
    MeasurementRepository measurementRepository;
    StatusService statusService;

    public SensorService(MeasurementRepository measurementRepository, StatusService statusService) {
        this.measurementRepository = measurementRepository;
        this.statusService = statusService;
    }

    public void collectMeasurement(MeasurementDto dto) {
        Measurement measurement = new Measurement()
                .setSensorId(dto.getSensorId())
                .setCo2(dto.getCo2())
                .setTime(dto.getTime());
        measurementRepository.save(measurement);
        statusService.updateStatusAsync(measurement);
    }
}
