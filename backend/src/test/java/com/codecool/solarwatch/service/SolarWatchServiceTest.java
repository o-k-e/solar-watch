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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.SET;
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

    @Mock
    private City mockCity;

    @Mock
    private SunriseSunset mockSunriseSunset;

    @InjectMocks
    private SolarWatchService solarWatchService;

    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2025, 3, 18);
    }

    @DisplayName("getSunriseSunset - existing city with cached data")
    @Test
    void givenCityAndCachedData_whenGetSunriseSunset_thenReturnCachedResponse() {
        // GIVEN
        given(cityRepository.findByName("London")).willReturn(Optional.of(mockCity));
        given(mockCity.getSunriseSunsets()).willReturn(List.of(mockSunriseSunset));
        given(mockSunriseSunset.getDate()).willReturn(testDate);
        given(mockSunriseSunset.getSunrise()).willReturn("06:30 AM");
        given(mockSunriseSunset.getSunset()).willReturn("06:45 PM");
        given(mockCity.getName()).willReturn("London");

        // WHEN
        SolarWatchResponse response = solarWatchService.getSunriseSunset("London", testDate);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.city()).isEqualTo("London");
        assertThat(response.sunrise()).isEqualTo("06:30 AM");
        assertThat(response.sunset()).isEqualTo("06:45 PM");
    }

    @DisplayName("getSunriseSunset - existing city with no cached data")
    @Test
    void givenCityWithoutCachedData_whenGetSunriseSunset_thenFetchFromApi() {
        // GIVEN
        SunriseSunsetResponse apiResponse = new SunriseSunsetResponse(
                new SunriseSunsetResults("06:30 AM", "06:45 PM"));

        List<SunriseSunset> sunriseList = new ArrayList<>(); // ✅ Mutable List
        given(cityRepository.findByName("London")).willReturn(Optional.of(mockCity));
        given(mockCity.getSunriseSunsets()).willReturn(sunriseList); // ✅ Correct type
        given(mockCity.getLatitude()).willReturn(51.5074);
        given(mockCity.getLongitude()).willReturn(-0.1276);
        given(mockCity.getName()).willReturn("London");
        given(sunriseSunsetService.getSunriseSunset(51.5074, -0.1276, testDate)).willReturn(Optional.of(apiResponse));
        given(cityRepository.save(mockCity)).willReturn(mockCity);

        // WHEN
        SolarWatchResponse response = solarWatchService.getSunriseSunset("London", testDate);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.city()).isEqualTo("London");
        assertThat(response.sunrise()).isEqualTo("06:30 AM");
        assertThat(response.sunset()).isEqualTo("06:45 PM");
    }

    @DisplayName("getSunriseSunset - city not in DB, fetched and saved from API")
    @Test
    void givenCityNotInDb_whenGetSunriseSunset_thenFetchCityThenFetchData() {
        // GIVEN
        GeocodingResponse geocode = new GeocodingResponse("London", 51.5074, -0.1276, "GB");
        CityResponse cityResponse = new CityResponse(1L, "London", -0.1276, 51.5074, "GB");
        SunriseSunsetResponse apiResponse = new SunriseSunsetResponse(
                new SunriseSunsetResults("06:30 AM", "06:45 PM"));

        List<SunriseSunset> sunriseList = new ArrayList<>();
        given(cityRepository.findByName("London")).willReturn(Optional.empty());
        given(geocodingService.getCoordinates("London")).willReturn(Optional.of(geocode));
        given(cityService.addCity(any(CityCreationRequest.class))).willReturn(cityResponse);
        given(cityMapper.mapToCity(cityResponse)).willReturn(mockCity);
        given(mockCity.getSunriseSunsets()).willReturn(sunriseList);
        given(mockCity.getLatitude()).willReturn(51.5074);
        given(mockCity.getLongitude()).willReturn(-0.1276);
        given(mockCity.getName()).willReturn("London");
        given(sunriseSunsetService.getSunriseSunset(51.5074, -0.1276, testDate)).willReturn(Optional.of(apiResponse));
        given(cityRepository.save(mockCity)).willReturn(mockCity);

        // WHEN
        SolarWatchResponse response = solarWatchService.getSunriseSunset("London", testDate);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.city()).isEqualTo("London");
    }

    @DisplayName("getSunriseSunset - city not found in DB or API")
    @Test
    void givenCityNotFoundAnywhere_whenGetSunriseSunset_thenThrowException() {
        // GIVEN
        given(cityRepository.findByName("Atlantis")).willReturn(Optional.empty());
        given(geocodingService.getCoordinates("Atlantis")).willReturn(Optional.empty());

        // THEN
        assertThrows(CityNotFoundException.class, () ->
                solarWatchService.getSunriseSunset("Atlantis", testDate));
    }
}