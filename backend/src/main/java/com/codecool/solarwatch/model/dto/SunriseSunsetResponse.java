package com.codecool.solarwatch.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SunriseSunsetResponse(SunriseSunsetResults results) {

}

//{
//    "results": {
//        "sunrise": "6:30:00 AM",
//        "sunset": "5:45:00 PM"
//    },
//    "status": "OK"
//}
