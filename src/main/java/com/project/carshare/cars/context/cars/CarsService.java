package com.project.carshare.cars.context.cars;

import com.project.carshare.cars.context.cars.dto.AddCarRequestDto;
import com.project.carshare.cars.context.cars.dto.CarInfoResponseDto;
import com.project.carshare.cars.domain.Car;
import com.project.carshare.cars.domain.CarsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarsService {

    private final CarsRepository carsRepository;

    @SuppressWarnings("unchecked")
    private boolean isAdmin(){
        return ((Map<String,String>) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .get("role")
                .equals("ADMIN");
    }

    public List<CarInfoResponseDto> getAllCars() {
        if(!isAdmin()){
            throw new RuntimeException("You must be admin to do that");
        }

        return carsRepository.findAll().stream().map(it -> CarInfoResponseDto.builder()
                        .id(it.getId())
                        .make(it.getMake())
                        .model(it.getModel())
                        .carType(it.getCarType())
                        .fuelConsumption(it.getFuelConsumption())
                        .fuelType(it.getFuelType())
                        .horsePower(it.getHorsePower())
                        .available(it.isAvailable())
                        .build())
                .toList();
    }

    public void addCar(AddCarRequestDto request) {
        if(!isAdmin()){
            throw new RuntimeException("You must be admin to do that");
        }

        var car = Car.builder()
                .id(UUID.randomUUID())
                .make(request.getMake())
                .model(request.getModel())
                .carType(request.getCarType())
                .fuelConsumption(request.getFuelConsumption())
                .fuelType(request.getFuelType())
                .horsePower(request.getHorsePower())
                .available(false)
                .build();

        carsRepository.save(car);
    }
}
