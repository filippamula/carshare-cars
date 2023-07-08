package com.project.carshare.cars.context.cars.dto;

import com.project.carshare.cars.domain.enums.CarType;
import com.project.carshare.cars.domain.enums.FuelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarRequestDto {

    private String make;
    private String model;
    private CarType carType;
    private String fuelConsumption;
    private FuelType fuelType;
    private Integer horsePower;
}
