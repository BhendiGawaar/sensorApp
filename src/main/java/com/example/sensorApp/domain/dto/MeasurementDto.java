package com.example.sensorApp.domain.dto;

import com.example.sensorApp.domain.entity.Measurement;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Vishal Maral
 * Date: 10/7/21
 */
public class MeasurementDto {
    private String sensorId;
    private int co2;
    private ZonedDateTime time;

    public MeasurementDto() {
    }

    public MeasurementDto(int co2, ZonedDateTime time) {
        this.co2 = co2;
        this.time = time;
    }

    public String getSensorId() {
        return sensorId;
    }

    public MeasurementDto setSensorId(String sensorId) {
        this.sensorId = sensorId;
        return this;
    }

    public int getCo2() {
        return co2;
    }

    public MeasurementDto setCo2(int co2) {
        this.co2 = co2;
        return this;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public MeasurementDto setTime(ZonedDateTime time) {
        this.time = time;
        return this;
    }
}
