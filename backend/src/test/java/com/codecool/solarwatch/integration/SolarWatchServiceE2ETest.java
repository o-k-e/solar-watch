//package com.codecool.solarwatch.integration;
//
//import com.codecool.solarwatch.model.entity.City;
//import com.codecool.solarwatch.model.entity.SunriseSunset;
//import com.codecool.solarwatch.repository.CityRepository;
//import com.codecool.solarwatch.repository.SunriseSunsetRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.*;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
//public class SolarWatchServiceE2ETest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private CityRepository cityRepository;
//
//    @Autowired
//    private SunriseSunsetRepository sunriseSunsetRepository;
//
//    private String baseUrl;
//
//    @BeforeEach
//    void setUp() {
//        baseUrl = "http://localhost:" + port + "/solarwatch";
//    }
//
//    @DisplayName("Integration test - Fetch sunrise/sunset when data exists")
//    @Test
//    void givenCityWithData_whenGetSunriseSunset_thenReturnData() {
//        // GIVEN
//        City testCity = new City("London", -0.1276, 51.5074, "GB");
//        cityRepository.save(testCity);
//
//        SunriseSunset testSunriseSunset = new SunriseSunset(LocalDate.now(), "6:18:43 AM", "6:01:37 PM");
//        testCity.getSunriseSunsets().add(testSunriseSunset);
//        sunriseSunsetRepository.save(testSunriseSunset);
//
//        String jwtToken = "eyJhbGciOiJIUzI1NiJ9...";  // Replace with a fresh token
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + jwtToken);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        // WHEN
//        ResponseEntity<String> response = restTemplate.exchange(
//                baseUrl + "?city=London",
//                HttpMethod.GET,
//                entity,
//                String.class
//        );
//
//        // THEN
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).contains("6:18:43 AM");
//        assertThat(response.getBody()).contains("6:01:37 PM");
//    }
//
//    @DisplayName("Integration test - Fetch city when not in database")
//    @Test
//    void givenCityNotInDatabase_whenGetSunriseSunset_thenFetchFromAPI() {
//        // GIVEN
//        assertThat(cityRepository.findByName("Paris")).isEmpty();
//
//        String jwtToken = "eyJhbGciOiJIUzI1NiJ9...";  // Replace with a fresh token
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + jwtToken);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        // WHEN
//        ResponseEntity<String> response = restTemplate.exchange(
//                baseUrl + "?city=Paris",
//                HttpMethod.GET,
//                entity,
//                String.class
//        );
//
//        // THEN
//        Optional<City> savedCity = cityRepository.findByName("Paris");
//        assertThat(savedCity).isPresent();
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//}