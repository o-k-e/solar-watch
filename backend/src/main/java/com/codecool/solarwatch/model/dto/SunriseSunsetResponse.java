package com.codecool.solarwatch.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SunriseSunsetResponse(SunriseSunsetResults results) {

}
