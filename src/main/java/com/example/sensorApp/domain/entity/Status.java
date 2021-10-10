package com.example.sensorApp.domain.entity;

import com.example.sensorApp.util.StatusEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/**
 * @author Vishal Maral
 * Date: 10/8/21
 */

@Entity
public class Status {
    @Id
    private String sensorId;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    public Status() {
    }

    public Status(String sensorId, StatusEnum status) {
        this.sensorId = sensorId;
        this.status = status;
    }

    public String getSensorId() {
        return sensorId;
    }

    public Status setSensorId(String sensorId) {
        this.sensorId = sensorId;
        return this;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public Status setStatus(StatusEnum status) {
        this.status = status;
        return this;
    }
}
