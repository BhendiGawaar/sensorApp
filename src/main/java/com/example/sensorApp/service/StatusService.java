package com.example.sensorApp.service;

import com.example.sensorApp.domain.dto.StatusDto;
import com.example.sensorApp.domain.entity.Measurement;
import com.example.sensorApp.domain.entity.Status;
import com.example.sensorApp.repository.MeasurementRepository;
import com.example.sensorApp.repository.StatusRepository;
import com.example.sensorApp.util.StatusEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Vishal Maral
 * Date: 10/8/21
 */
@Service
public class StatusService {
    @Value("${acceptableCo2Level}")
    private int acceptableCo2Level;
    @Value("${acceptableHighCount}")
    private int acceptableHighCount;

    MeasurementRepository measurementRepository;
    StatusRepository statusRepository;

    public StatusService(MeasurementRepository measurementRepository, StatusRepository statusRepository) {
        this.measurementRepository = measurementRepository;
        this.statusRepository = statusRepository;
    }

    public void updateStatusAsync(Measurement measurement){
        ExecutorService executorService = Executors.newCachedThreadPool();
        Runnable updateStatusTask = () -> {
            try {
                updateStatus(measurement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        executorService.execute(updateStatusTask);
    }
    public void updateStatus(Measurement measurement){
        String sensorId = measurement.getSensorId();
        int co2Level = measurement.getCo2();

        Optional<Status> optCurrentStatus = statusRepository.findBySensorId(sensorId);
        //if it is a first measurement
        if(!optCurrentStatus.isPresent()){
            statusRepository.save(new Status(sensorId, co2Level > acceptableCo2Level ? StatusEnum.WARN:StatusEnum.OK));
        }else{
            StatusEnum newStatus = null;
            StatusEnum currentStatus = optCurrentStatus.get().getStatus();
            //WARN condition
            if(currentStatus == StatusEnum.OK && co2Level > acceptableCo2Level){
                newStatus = StatusEnum.WARN;
            }else{
                List<Measurement> measurements = measurementRepository
                        .findBySensorIdOrderByTimeDesc(sensorId, PageRequest.of(0, acceptableHighCount));

                if(currentStatus == StatusEnum.WARN) {
                    if(co2Level <= acceptableCo2Level){
                        newStatus = StatusEnum.OK;
                    }else{
                        boolean isAlertCondition = measurements
                                .stream()
                                .filter(m -> m.getCo2() > acceptableCo2Level)
                                .count() == acceptableHighCount;
                        if (isAlertCondition) {
                            newStatus = StatusEnum.ALERT;
                        }
                    }
                }else if(currentStatus == StatusEnum.ALERT) {
                    boolean isOkCondition = measurements
                            .stream()
                            .filter(m -> m.getCo2() <= acceptableCo2Level)
                            .count() == acceptableHighCount;
                    if (isOkCondition) {
                        newStatus = StatusEnum.OK;
                    }
                }
            }

            if(newStatus != null && newStatus != currentStatus){
                statusRepository.save(new Status(sensorId, newStatus));
            }
        }
    }

    public StatusDto getStatus(String sensorID) throws Exception{
        Optional<Status> optionalStatus = statusRepository.findBySensorId(sensorID);
        if(optionalStatus.isPresent()) {
            return new StatusDto(optionalStatus.get().getStatus().name());
        }else{
            throw new Exception("Status not present");
        }

    }
}
