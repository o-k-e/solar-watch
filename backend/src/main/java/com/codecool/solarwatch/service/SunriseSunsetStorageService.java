package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.dto.SunriseSunsetResponse;
import com.codecool.solarwatch.model.entity.SunriseSunset;
import com.codecool.solarwatch.repository.SunriseSunsetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SunriseSunsetStorageService {

    private final SunriseSunsetRepository sunriseSunsetRepository;

    @Autowired
    public SunriseSunsetStorageService(SunriseSunsetRepository sunriseSunsetRepository) {
        this.sunriseSunsetRepository = sunriseSunsetRepository;
    }

    public List<SunriseSunset> getAllSunriseSunsets() {
        return sunriseSunsetRepository.findAll();
    }

    public void save(SunriseSunset sunriseSunset) {
        sunriseSunsetRepository.save(sunriseSunset);
    }
}
