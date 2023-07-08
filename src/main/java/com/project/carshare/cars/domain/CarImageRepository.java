package com.project.carshare.cars.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarImageRepository extends JpaRepository<CarImage, UUID> {
}
