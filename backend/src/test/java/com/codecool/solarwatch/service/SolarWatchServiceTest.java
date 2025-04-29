package com.codecool.solarwatch.service;

import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.model.dto.GeocodingResponse;
import com.codecool.solarwatch.model.dto.SolarWatchResponse;
import com.codecool.solarwatch.model.dto.SunriseSunsetResponse;
import com.codecool.solarwatch.model.dto.SunriseSunsetResults;
import com.codecool.solarwatch.model.dto.request.CityCreationRequest;
import com.codecool.solarwatch.model.dto.response.CityResponse;
import com.codecool.solarwatch.model.entity.City;
import com.codecool.solarwatch.model.entity.SunriseSunset;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.service.mapper.CityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SolarWatchServiceTest {

    @Mock
    private GeocodingService geocodingService;

    @Mock
    private SunriseSunsetService sunriseSunsetService;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityService cityService;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private SolarWatchService solarWatchService;

    private City testCity;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testCity = new City("London", -0.1276, 51.5074, "GB");
        testDate = LocalDate.of(2025, 3, 18);
    }

    @DisplayName("JUnit test for getSunriseSunset() when city and data exist")
    @Test
    void givenCityAndDataExists_whenGetSunriseSunset_thenReturnResponse() {
        // GIVEN
        given(cityRepository.findByName("London")).willReturn(Optional.of(testCity));
        given(sunriseSunsetService.getSunriseSunset(testCity.getLatitude(), testCity.getLongitude(), testDate))
                .willReturn(Optional.of(new SunriseSunsetResponse(new SunriseSunsetResults("06:30 AM", "06:45 PM"))));

        // WHEN
        Optional<SolarWatchResponse> response = solarWatchService.getSunriseSunset("London", testDate);

        // THEN
        assertThat(response).isPresent();
        assertThat(response.get().city()).isEqualTo("London");
        assertThat(response.get().sunrise()).isEqualTo("06:30 AM");
        assertThat(response.get().sunset()).isEqualTo("06:45 PM");
    }

    @DisplayName("JUnit test for getSunriseSunset() when city exists but no data")
    @Test
    void givenCityExistsNoData_whenGetSunriseSunset_thenFetchFromAPI() {
        // GIVEN
        given(cityRepository.findByName("London")).willReturn(Optional.of(testCity));
        given(sunriseSunsetService.getSunriseSunset(testCity.getLatitude(), testCity.getLongitude(), testDate))
                .willReturn(Optional.of(new SunriseSunsetResponse(new SunriseSunsetResults("06:30 AM", "06:45 PM"))));

        // WHEN
        Optional<SolarWatchResponse> response = solarWatchService.getSunriseSunset("London", testDate);

        // THEN
        assertThat(response).isPresent();
        assertThat(response.get().city()).isEqualTo("London");
    }

    @DisplayName("JUnit test for getSunriseSunset() when city does not exist")
    @Test
    void givenCityDoesNotExist_whenGetSunriseSunset_thenFetchAndSaveCity() {
        // GIVEN
        given(cityRepository.findByName("London")).willReturn(Optional.empty());
        given(geocodingService.getCoordinates("London"))
                .willReturn(Optional.of(new GeocodingResponse("London", 51.5074, -0.1276, "GB")));

        given(cityService.addCity(any(CityCreationRequest.class)))
                .willReturn(new CityResponse(1L, "London", -0.1276, 51.5074, "GB"));

        given(cityMapper.mapToCity(any(CityResponse.class)))
                .willReturn(testCity);

        given(sunriseSunsetService.getSunriseSunset(testCity.getLatitude(), testCity.getLongitude(), testDate))
                .willReturn(Optional.of(new SunriseSunsetResponse(new SunriseSunsetResults("06:30 AM", "06:45 PM"))));

        // WHEN
        Optional<SolarWatchResponse> response = solarWatchService.getSunriseSunset("London", testDate);

        // THEN
        assertThat(response).isPresent();
        assertThat(response.get().city()).isEqualTo("London");
    }

    @DisplayName("JUnit test for getSunriseSunset() when city is not found in API")
    @Test
    void givenCityNotFound_whenGetSunriseSunset_thenThrowException() {
        // GIVEN
        given(cityRepository.findByName("UnknownCity")).willReturn(Optional.empty());
        given(geocodingService.getCoordinates("UnknownCity")).willReturn(Optional.empty());

        // THEN
        assertThrows(CityNotFoundException.class, () -> solarWatchService.getSunriseSunset("UnknownCity", testDate));
    }
}