package com.codecool.solarwatch.service;


import com.codecool.solarwatch.model.dto.SunriseSunsetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SunriseSunsetService {

    private static final String API_URL = "https://api.sunrise-sunset.org/json";
    private static final Logger logger = LoggerFactory.getLogger(SunriseSunsetService.class);

    private final RestTemplate restTemplate;

    public SunriseSunsetService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Retrieves the sunrise and sunset times for a given geographic location and date
     * by calling an external sunrise/sunset API.
     *
     * @param lat  the latitude of the location
     * @param lon  the longitude of the location
     * @param date the date for which sunrise and sunset times are requested
     * @return an {@link Optional} containing the {@link SunriseSunsetResponse} if data is available,
     *         or {@link Optional#empty()} if the response or its results are null
     */
    public Optional<SunriseSunsetResponse> getSunriseSunset(double lat, double lon, LocalDate date) {
        logger.info("Getting sunrise sunset for lat: {}, lon: {}, date: {}", lat, lon, date);

        String url = API_URL + "?lat=" + lat + "&lng=" + lon + "&date=" + date;

        SunriseSunsetResponse response = restTemplate.getForObject(url, SunriseSunsetResponse.class);

        if (response == null || response.results() == null) {
            logger.warn("No sunrise/sunset data found for request: {}", url);
            return Optional.empty();
        }
        return Optional.of(response);
    }
}
