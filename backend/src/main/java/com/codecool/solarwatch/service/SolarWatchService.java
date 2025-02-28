package com.codecool.solarwatch.service;

import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.model.dto.GeocodingResponse;
import com.codecool.solarwatch.model.dto.SolarWatchResponse;
import com.codecool.solarwatch.model.dto.SunriseSunsetResponse;
import com.codecool.solarwatch.model.dto.request.CityCreationRequest;
import com.codecool.solarwatch.model.dto.response.CityResponse;
import com.codecool.solarwatch.model.entity.City;
import com.codecool.solarwatch.model.entity.SunriseSunset;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseSunsetRepository;
import com.codecool.solarwatch.service.mapper.CityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SolarWatchService {

    private static final Logger logger = LoggerFactory.getLogger(SolarWatchService.class);

    private final GeocodingService geocodingService;
    private final SunriseSunsetService sunriseSunsetService;
    private final CityRepository cityRepository;
//    private final SunriseSunsetRepository sunriseSunsetRepository;
    private final SunriseSunsetStorageService sunriseSunsetStorageService;
    private final CityService cityService;
    private final CityMapper cityMapper;

    public SolarWatchService(
            GeocodingService geocodingService,
            SunriseSunsetService sunriseSunsetService,
            CityRepository cityRepository,
            SunriseSunsetRepository sunriseSunsetRepository,
            SunriseSunsetStorageService sunriseSunsetStorageService, CityService cityService, CityMapper cityMapper) {
        this.geocodingService = geocodingService;
        this.sunriseSunsetService = sunriseSunsetService;
        this.cityRepository = cityRepository;
//        this.sunriseSunsetRepository = sunriseSunsetRepository;
        this.sunriseSunsetStorageService = sunriseSunsetStorageService;
        this.cityService = cityService;
        this.cityMapper = cityMapper;
    }

    public Optional<SolarWatchResponse> getSunriseSunset(String cityName, LocalDate date) {
        logger.info("Getting sunrise sunset for city: {}, date: {}", cityName, date);

        //Check if city exists in the database
        Optional<City> cityOptional = cityRepository.findByName(cityName);

        City city;
        if (cityOptional.isPresent()) {
            city = cityOptional.get();
            logger.info("City {} found in database.", city.getName());
        } else {
            logger.info("City {} not found in database. Fetching from API...", cityName);
            Optional<GeocodingResponse> geocodingResponse = geocodingService.getCoordinates(cityName);

            if (geocodingResponse.isEmpty()) {
                throw new CityNotFoundException(cityName);
            }

            //Save new city to database
            CityResponse cityResponse = cityService.addCity(new CityCreationRequest(
                    cityName,
                    geocodingResponse.get().lon(),
                    geocodingResponse.get().lat(),
                    geocodingResponse.get().country()
            ));
            city = cityMapper.mapToCity(cityResponse);
            logger.info("City {} saved in database.", cityName);
        }

        //Check if sunrise/sunset data exists for the given date
        Optional<SunriseSunset> existingSunriseSunset = city.getSunriseSunsets().stream()
                .filter(ss -> ss.getDate().equals(date))
                .findFirst();

        if (existingSunriseSunset.isPresent()) {
            logger.info("Sunrise/sunset data found in database for city {}, date {}", cityName, date);
            SunriseSunset sunriseSunset = existingSunriseSunset.get();
            return Optional.of(new SolarWatchResponse(
                    city.getName(),
                    date,
                    sunriseSunset.getSunrise(),
                    sunriseSunset.getSunset()
            ));
        }

        //Fetch from API if data is missing
        logger.info("Sunrise/sunset data not found in database. Fetching from API...");
        Optional<SunriseSunsetResponse> sunriseSunsetResponse = sunriseSunsetService.getSunriseSunset(
                city.getLatitude(), city.getLongitude(), date);

        if (sunriseSunsetResponse.isEmpty()) {
            logger.warn("Sunrise/sunset data not found for city: {}", cityName);
            return Optional.empty();
        }

        //Save fetched sunrise/sunset data to database
        SunriseSunset newSunriseSunset = new SunriseSunset(
                date,
                sunriseSunsetResponse.get().results().sunrise(),
                sunriseSunsetResponse.get().results().sunset()
        );


//        sunriseSunsetStorageService.save(newSunriseSunset);
        city.getSunriseSunsets().add(newSunriseSunset);
        cityRepository.save(city);

        logger.info("Sunrise/sunset data saved for city: {}, date: {}", cityName, date);

        return Optional.of(new SolarWatchResponse(
                city.getName(),
                date,
                newSunriseSunset.getSunrise(),
                newSunriseSunset.getSunset()
        ));
    }
}