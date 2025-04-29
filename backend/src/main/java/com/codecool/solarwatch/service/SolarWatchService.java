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
    private final CityService cityService;
    private final CityMapper cityMapper;

    public SolarWatchService(
            GeocodingService geocodingService,
            SunriseSunsetService sunriseSunsetService,
            CityRepository cityRepository,
            CityService cityService,
            CityMapper cityMapper) {
        this.geocodingService = geocodingService;
        this.sunriseSunsetService = sunriseSunsetService;
        this.cityRepository = cityRepository;
        this.cityService = cityService;
        this.cityMapper = cityMapper;
    }

    /**
     * Fetches sunrise/sunset data for a city and date.
     *
     * @param cityName The city name.
     * @param date     The requested date.
     * @return Optional SolarWatchResponse containing sunrise/sunset times.
     */
    public SolarWatchResponse getSunriseSunset(String cityName, LocalDate date) {
        logger.info("Getting sunrise sunset for city: {}, date: {}", cityName, date);

        // Fetch city from DB or API
        City city = cityRepository.findByName(cityName)
                .orElseGet(() -> fetchAndSaveCity(cityName));

        // Check if sunrise/sunset data exists in DB for this date
        Optional<SunriseSunset> existingSunriseSunset = getExistingSunriseSunset(city, date);
        if (existingSunriseSunset.isPresent()) {
            logger.info("Returning cached sunrise/sunset data for city {}, date {}", cityName, date);
            return mapToSolarWatchResponse(city, date, existingSunriseSunset.get());
        }

        // Fetch from API if not found
        SunriseSunset newSunriseSunset = fetchAndSaveSunriseSunset(city, date);
        return mapToSolarWatchResponse(city, date, newSunriseSunset);
    }

    /**
     * Fetches a city from the Geocoding API and saves it in the database.
     *
     * @param cityName The city name.
     * @return The newly saved City entity.
     */
    private City fetchAndSaveCity(String cityName) {
        logger.info("City {} not found in database. Fetching from API...", cityName);
        GeocodingResponse geocodingResponse = geocodingService.getCoordinates(cityName)
                .orElseThrow(() -> new CityNotFoundException(cityName));

        CityResponse cityResponse = cityService.addCity(new CityCreationRequest(
                cityName,
                geocodingResponse.lon(),
                geocodingResponse.lat(),
                geocodingResponse.country()
        ));
        City city = cityMapper.mapToCity(cityResponse);
        logger.info("City {} saved in database.", cityName);
        return city;
    }

    /**
     * Retrieves sunrise/sunset data from the database if it exists.
     *
     * @param city The city entity.
     * @param date The requested date.
     * @return Optional SunriseSunset entity.
     */
    private Optional<SunriseSunset> getExistingSunriseSunset(City city, LocalDate date) {
        return city.getSunriseSunsets().stream()
                .filter(ss -> ss.getDate().equals(date))
                .findFirst();
    }

    /**
     * Fetches sunrise/sunset data from an external API and saves it.
     *
     * @param city The city entity.
     * @param date The requested date.
     * @return The saved SunriseSunset entity.
     */
    private SunriseSunset fetchAndSaveSunriseSunset(City city, LocalDate date) {
        logger.info("Fetching sunrise/sunset data from API for city {}, date {}", city.getName(), date);
        SunriseSunsetResponse sunriseSunsetResponse = sunriseSunsetService.getSunriseSunset(
                        city.getLatitude(), city.getLongitude(), date)
                .orElseThrow(() -> new CityNotFoundException(city.getName()));

        SunriseSunset newSunriseSunset = new SunriseSunset(
                date,
                sunriseSunsetResponse.results().sunrise(),
                sunriseSunsetResponse.results().sunset()
        );

        city.getSunriseSunsets().add(newSunriseSunset);
        cityRepository.save(city);

        logger.info("Sunrise/sunset data saved for city {}, date {}", city.getName(), date);
        return newSunriseSunset;
    }

    /**
     * Converts SunriseSunset entity to SolarWatchResponse DTO.
     *
     * @param city           The city entity.
     * @param date           The requested date.
     * @param sunriseSunset  The sunrise/sunset entity.
     * @return The mapped SolarWatchResponse DTO.
     */
    private SolarWatchResponse mapToSolarWatchResponse(City city, LocalDate date, SunriseSunset sunriseSunset) {
        return new SolarWatchResponse(
                city.getName(),
                date,
                sunriseSunset.getSunrise(),
                sunriseSunset.getSunset()
        );
    }
}