package com.codecool.solarwatch.service.mapper;

import com.codecool.solarwatch.model.dto.request.CityCreationRequest;
import com.codecool.solarwatch.model.dto.response.CityResponse;
import com.codecool.solarwatch.model.entity.City;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CityMapper {

    public CityResponse mapToCityResponse(City city) {
        CityResponse cityResponse = new CityResponse();
        cityResponse.setId(city.getId());
        cityResponse.setName(city.getName());
        cityResponse.setLongitude(city.getLongitude());
        cityResponse.setLatitude(city.getLatitude());
        cityResponse.setCountry(city.getCountry());
        return cityResponse;
    }

    public List<CityResponse> mapToCityResponseList(List<City> cities) {
        return cities.stream()
                .map(this::mapToCityResponse)
                .toList();
    }

    public City mapToCity(CityResponse cityResponse) {
        City city = new City();
        city.setId(cityResponse.getId());
        city.setName(cityResponse.getName());
        city.setLongitude(cityResponse.getLongitude());
        city.setLatitude(cityResponse.getLatitude());
        city.setCountry(cityResponse.getCountry());
        return city;
    }

    public City mapToCity(CityCreationRequest cityCreationRequest) {
        City city = new City();
        city.setName(cityCreationRequest.name());
        city.setLongitude(cityCreationRequest.longitude());
        city.setLatitude(cityCreationRequest.latitude());
        city.setCountry(cityCreationRequest.country());
        return city;
    }
}
