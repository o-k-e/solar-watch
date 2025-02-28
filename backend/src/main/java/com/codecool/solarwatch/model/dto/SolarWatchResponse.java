package com.codecool.solarwatch.model.dto;

import java.time.LocalDate;

public record SolarWatchResponse(
        String city,
        LocalDate date,
        String sunrise,
        String sunset
) {}
