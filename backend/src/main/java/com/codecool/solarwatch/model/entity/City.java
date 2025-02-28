package com.codecool.solarwatch.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private double longitude;
    private double latitude;
    private String country;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "city_id")
    private List<SunriseSunset> sunriseSunsets = new ArrayList<>();

    public City(String name, double longitude, double latitude, String country) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.country = country;
    }

    public City() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<SunriseSunset> getSunriseSunsets() {
        return sunriseSunsets;
    }

    public void setSunriseSunsets(List<SunriseSunset> sunriseSunsets) {
        this.sunriseSunsets = sunriseSunsets;
    }
}
