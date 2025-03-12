package com.codecool.solarwatch.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityResponse {

    private long id;
    private String name;
    private double longitude;
    private double latitude;
    private String country;
}
