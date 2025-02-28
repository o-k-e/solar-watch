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

    public Optional<SunriseSunsetResponse> getSunriseSunset(double lat, double lon, LocalDate date) {
        logger.info("Getting sunrise sunset for lat: {}, lon: {}, date: {}", lat, lon, date);

        String url = API_URL + "?lat=" + lat + "&lng=" + lon + "&date=" + date;
//        String url = String.format("https://api.sunrise-sunset.org/json?lat=%s&lng=%s&date=%s", lat, lon, date);

//        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
//                .queryParam("lat", lat)
//                .queryParam("lng", lon)
//                .queryParam("date", date)
//                .toUriString();

        SunriseSunsetResponse response = restTemplate.getForObject(url, SunriseSunsetResponse.class);

        if (response == null || response.results() == null) {
            logger.warn("No sunrise/sunset data found for request: {}", url);
            return Optional.empty();
        }
        return Optional.of(response);
    }
}
