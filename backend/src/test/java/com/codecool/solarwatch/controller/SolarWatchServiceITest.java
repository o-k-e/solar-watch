package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.entity.City;
import com.codecool.solarwatch.model.entity.SunriseSunset;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseSunsetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@Transactional
public class SolarWatchServiceITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private SunriseSunsetRepository sunriseSunsetRepository;

    @BeforeEach
    void setUp() {
        // Clear DB before each test
        sunriseSunsetRepository.deleteAll();
        cityRepository.deleteAll();
    }

    @DisplayName("Integration test - Fetch sunrise/sunset when data exists")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenCityWithData_whenGetSunriseSunset_thenReturnData() throws Exception {
        // GIVEN
        City testCity = new City("London", -0.1276474, 51.5073219, "GB");
        cityRepository.save(testCity);

        SunriseSunset testSunriseSunset = new SunriseSunset(LocalDate.now(), "6:18:43 AM", "6:01:37 PM");
        testCity.getSunriseSunsets().add(testSunriseSunset);
        sunriseSunsetRepository.save(testSunriseSunset);

        // WHEN & THEN
        mockMvc.perform(get("/solarwatch")
                        .param("city", "London")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("London"))
                .andExpect(jsonPath("$.sunrise").value("6:18:43 AM"))
                .andExpect(jsonPath("$.sunset").value("6:01:37 PM"));
    }

    @DisplayName("Integration test - Fetch city when not in database")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenCityNotInDatabase_whenGetSunriseSunset_thenFetchFromAPI() throws Exception {
        // GIVEN
        assertThat(cityRepository.findByName("Paris")).isEmpty();

        // WHEN
        mockMvc.perform(get("/solarwatch")
                        .param("city", "Paris")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // THEN
        Optional<City> savedCity = cityRepository.findByName("Paris");
        assertThat(savedCity).isPresent(); // City should now exist in DB
    }
}