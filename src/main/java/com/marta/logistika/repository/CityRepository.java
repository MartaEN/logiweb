package com.marta.logistika.repository;

import com.marta.logistika.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("cityRepository")
public interface CityRepository extends JpaRepository<City, Long> {

    @Query("select c from City c where c.id = :id")
    Optional<City> findById(@Param("id") long cityId);

}
