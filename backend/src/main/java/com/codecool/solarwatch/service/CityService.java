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

    public List<CityResponse> getAllCities() {
        List<City> cities = cityRepository.findAll();
        return cityMapper.mapToCityResponseList(cities);

    }

    public Optional<CityResponse> getCityById(long id) {
        Optional<City> city = cityRepository.findById(id);
        if (city.isPresent()) {
            CityResponse cityResponse = cityMapper.mapToCityResponse(city.get());
            return Optional.of(cityResponse);
        } else {
            log.warn("City with id {} not found", id);
            return Optional.empty();
        }
    }

    public Optional<City> getCityByName(String name) {
        return cityRepository.findByName(name);
    }

    public CityResponse addCity(CityCreationRequest city) {
        log.info("1. Creating city name: {}, country: {}", city.getName(), city.getCountry());
        City savedCity = cityRepository.save(cityMapper.mapToCity(city));
        CityResponse cityResponse = cityMapper.mapToCityResponse(savedCity);
        log.info("2. New city name: {} saved.", savedCity.getName());
        return cityResponse;
    }

    public SuccessResponse deleteCity(long id) {

        Optional<City> city = cityRepository.findById(id);
        City cityEntity = city.orElseThrow(() -> new CityNotFoundException(Long.toString(id)));
        cityRepository.delete(cityEntity);
        return new SuccessResponse(true);
    }

    public SuccessResponse updateCity(CityModificationRequest cityModificationRequest) {
        Optional<City> cityEntity = cityRepository.findById(cityModificationRequest.getId());
        City city = cityEntity.orElseThrow(() -> new CityNotFoundException(Long.toString(cityModificationRequest.getId())));
        city.setName(cityModificationRequest.getName());
        city.setLongitude(cityModificationRequest.getLongitude());
        city.setLatitude(cityModificationRequest.getLatitude());
        city.setCountry(cityModificationRequest.getCountry());
        cityRepository.save(city);
        return new SuccessResponse(true);
    }
}
