package com.example.sensorApp.domain.dto;

import com.example.sensorApp.util.StatusEnum;

/**
 * @author Vishal Maral
 * Date: 10/8/21
 */
public class StatusDto {
    private String status;

    public StatusDto() {
    }

    public StatusDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public StatusDto setStatus(String status) {
        this.status = status;
        return this;
    }
}
