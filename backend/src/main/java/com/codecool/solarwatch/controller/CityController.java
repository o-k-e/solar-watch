package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.model.dto.request.CityCreationRequest;
import com.codecool.solarwatch.model.dto.request.CityModificationRequest;
import com.codecool.solarwatch.model.dto.response.CityResponse;
import com.codecool.solarwatch.model.dto.response.SuccessResponse;
import com.codecool.solarwatch.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/find-all")
    public List<CityResponse> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/find-by-id/{id}")
    public CityResponse getCityById(@PathVariable long id) {
        return cityService.getCityById(id);
    }

    @PostMapping("/create")
    public CityResponse createCity(@RequestBody CityCreationRequest city) {
        return cityService.addCity(city);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public SuccessResponse deleteCityById(@PathVariable long id) {
        return cityService.deleteCity(id);
    }

    @PutMapping("/modify")
    public SuccessResponse modifyCity(@RequestBody CityModificationRequest cityModificationRequest) {
        return cityService.updateCity(cityModificationRequest);
    }


}
