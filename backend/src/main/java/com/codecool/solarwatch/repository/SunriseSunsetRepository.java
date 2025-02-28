package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.model.entity.SunriseSunset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface SunriseSunsetRepository extends JpaRepository<SunriseSunset, Long> {
    List<SunriseSunset> findByDate(LocalDate date);
}
