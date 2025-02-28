package com.codecool.solarwatch.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
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

    public SunriseSunset() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
}
