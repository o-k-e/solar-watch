package com.codecool.solarwatch.service;

import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.model.dto.request.CityCreationRequest;
import com.codecool.solarwatch.model.dto.request.CityModificationRequest;
import com.codecool.solarwatch.model.dto.response.CityResponse;
import com.codecool.solarwatch.model.dto.response.SuccessResponse;
import com.codecool.solarwatch.model.entity.City;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.service.mapper.CityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private static final Logger log = LoggerFactory.getLogger(CityService.class);
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Autowired
    public CityService(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    /**
     * Retrieves all cities stored in the database.
     *
     * @return a list of {@link CityResponse} objects representing all saved cities
     */
    public List<CityResponse> getAllCities() {
        List<City> cities = cityRepository.findAll();
        return cityMapper.mapToCityResponseList(cities);

    }

    /**
     * Retrieves a city by its unique identifier.
     *
     * @param id the ID of the city to retrieve
     * @return an {@link Optional} containing the {@link CityResponse} if the city exists,
     *         or {@link Optional#empty()} if no city with the given ID was found
     */
    public CityResponse getCityById(long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("City with id {} not found", id);
                    return new CityNotFoundException(Long.toString(id));
                });
        return cityMapper.mapToCityResponse(city);
    }

    /**
     * Creates and stores a new city in the database.
     *
     * @param city the {@link CityCreationRequest} containing the name, country, and coordinates of the city
     * @return a {@link CityResponse} representing the newly created city
     */
    public CityResponse addCity(CityCreationRequest city) {
        log.info("1. Creating city name: {}, country: {}", city.name(), city.country());
        City savedCity = cityRepository.save(cityMapper.mapToCity(city));
        CityResponse cityResponse = cityMapper.mapToCityResponse(savedCity);
        log.info("2. New city name: {} saved.", savedCity.getName());
        return cityResponse;
    }

    /**
     * Deletes a city from the database based on its ID.
     *
     * @param id the ID of the city to delete
     * @return a {@link SuccessResponse} indicating whether the operation was successful
     * @throws CityNotFoundException if no city with the given ID exists
     */
    public SuccessResponse deleteCity(long id) {

        Optional<City> city = cityRepository.findById(id);
        City cityEntity = city.orElseThrow(() -> new CityNotFoundException(Long.toString(id)));
        cityRepository.delete(cityEntity);
        return new SuccessResponse(true);
    }
    /**
     * Updates the information of an existing city.
     *
     * @param cityModificationRequest the {@link CityModificationRequest} containing updated city data
     * @return a {@link SuccessResponse} indicating whether the update was successful
     * @throws CityNotFoundException if no city with the given ID exists
     */
    public SuccessResponse updateCity(CityModificationRequest cityModificationRequest) {
        Optional<City> cityEntity = cityRepository.findById(cityModificationRequest.id());
        City city = cityEntity.orElseThrow(() -> new CityNotFoundException(Long.toString(cityModificationRequest.id())));
        city.setName(cityModificationRequest.name());
        city.setLongitude(cityModificationRequest.longitude());
        city.setLatitude(cityModificationRequest.latitude());
        city.setCountry(cityModificationRequest.country());
        cityRepository.save(city);
        return new SuccessResponse(true);
    }
}
