package com.project.carshare.cars.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RentsRepository extends JpaRepository<Rent, UUID> {
    List<Rent> findByCarId(UUID carId);
}
