package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.dto.GeocodingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GeocodingService {

    private static final String API_URL = "https://api.openweathermap.org/geo/1.0/direct";
    private static final String apiKey = "60126d2faa87db30301736cbac1e6566";
    private static final Logger logger = LoggerFactory.getLogger(GeocodingService.class);

    private final RestTemplate restTemplate;

    public GeocodingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<GeocodingResponse> getCoordinates(String city) {
        String url = API_URL + "?q=" + city + "&appid=" + apiKey;

        logger.info("Calling GEO API URL: {}", url);

        GeocodingResponse[] responseArray = restTemplate.getForObject(url, GeocodingResponse[].class);
        //restTemplate.exchange() utananezni

        if (responseArray == null || responseArray.length == 0) {
            logger.warn("No coordinates found for city: {}", city);
            return Optional.empty();
        }

        return Optional.of(responseArray[0]);
    }


}
