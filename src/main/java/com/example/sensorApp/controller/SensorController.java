package com.example.sensorApp.controller;

import com.example.sensorApp.domain.dto.MeasurementDto;
import com.example.sensorApp.domain.dto.StatusDto;
import com.example.sensorApp.service.SensorService;
import com.example.sensorApp.service.StatusService;
import org.springframework.web.bind.annotation.*;

/**
 * @author Vishal Maral
 * Date: 10/7/21
 */
@RestController
@RequestMapping("/api/v1/sensors")
public class SensorController {

    private SensorService sensorService;
    private StatusService statusService;

    public SensorController(SensorService sensorService, StatusService statusService) {
        this.sensorService = sensorService;
        this.statusService = statusService;
    }

    @GetMapping("/greeting")
    public @ResponseBody String greeting() {
        return "Hello, World";
    }

    @PostMapping("/{uuid}/mesurements")
    public void collectMeasurement(@RequestBody MeasurementDto dto, @PathVariable String uuid){
        dto.setSensorId(uuid);
        sensorService.collectMeasurement(dto);
    }

    @GetMapping("/{uuid}")
    public StatusDto getStatus(@PathVariable String uuid) throws Exception{
        return statusService.getStatus(uuid);
    }
}
