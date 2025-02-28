package com.codecool.solarwatch.model.dto.request;

import lombok.Data;

@Data
public class CityModificationRequest {

    private long id;
    private String name;
    private double longitude;
    private double latitude;
    private String country;
}
