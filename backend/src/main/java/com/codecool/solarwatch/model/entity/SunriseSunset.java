package com.codecool.solarwatch.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SunriseSunset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate date;
    private String sunrise;
    private String sunset;

    public SunriseSunset(LocalDate date, String sunrise, String sunset) {
        this.date = date;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }
}
