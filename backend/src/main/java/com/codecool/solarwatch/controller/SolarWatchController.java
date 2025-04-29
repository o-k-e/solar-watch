package com.codecool.solarwatch.controller;


import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.model.dto.SolarWatchResponse;
import com.codecool.solarwatch.service.SolarWatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
public class SolarWatchController {

    private static final Logger logger = LoggerFactory.getLogger(SolarWatchController.class);

    private final SolarWatchService solarWatchService;

    public SolarWatchController(SolarWatchService solarWatchService) {
        this.solarWatchService = solarWatchService;
    }

    @GetMapping("/solarwatch")
    public SolarWatchResponse getSunriseSunset(
            @RequestParam String city,
            @RequestParam(required = false) LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }
        logger.info("Received request for city: {}, date: {}", city, date);
//        Optional<SolarWatchResponse> response = solarWatchService.getSunriseSunset(city, date);
        return solarWatchService.getSunriseSunset(city, date);
    }
}
