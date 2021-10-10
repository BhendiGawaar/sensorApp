package com.example.sensorApp.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Vishal Maral
 * Date: 10/7/21
 */

@Entity
public class Measurement {
    @Id
    private String uuid;
    private String sensorId;
    private int co2;
    private ZonedDateTime time;

    public String getUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
        return uuid;
    }

    public Measurement setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getSensorId() {
        return sensorId;
    }

    public Measurement setSensorId(String sensorId) {
        this.sensorId = sensorId;
        return this;
    }

    public int getCo2() {
        return co2;
    }

    public Measurement setCo2(int co2) {
        this.co2 = co2;
        return this;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public Measurement setTime(ZonedDateTime time) {
        this.time = time;
        return this;
    }
}
