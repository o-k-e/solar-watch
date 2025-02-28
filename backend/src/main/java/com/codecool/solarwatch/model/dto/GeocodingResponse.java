package com.codecool.solarwatch.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeocodingResponse(String name, double lat, double lon, String country) {
}

//       [
//         {
//         "lat": 47.4979,
//         "lon": 19.0402,
//         "name": "London",
//         “country”: “GB”
//        }
//      ]

