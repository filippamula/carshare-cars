package com.project.carshare.cars.domain;

import com.project.carshare.cars.domain.enums.CarType;
import com.project.carshare.cars.domain.enums.FuelType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CARS")
@Entity
@Data
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String make;
    private String model;
    @Enumerated(EnumType.STRING)
    private CarType carType;
    private String fuelConsumption;
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    private int horsePower;
    @OneToMany(mappedBy = "car", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CarImage> carImages = new ArrayList<>();
    private boolean available;

    public void addCarImage(CarImage carImage){
        carImages.add(carImage);
    }
}
