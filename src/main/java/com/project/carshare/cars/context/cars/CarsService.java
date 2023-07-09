package com.project.carshare.cars.context.cars;

import com.project.carshare.cars.context.cars.dto.CarInfoResponseDto;
import com.project.carshare.cars.context.cars.dto.CarRequestDto;
import com.project.carshare.cars.domain.Car;
import com.project.carshare.cars.domain.CarImage;
import com.project.carshare.cars.domain.CarImageRepository;
import com.project.carshare.cars.domain.CarsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarsService {

    private final CarsRepository carsRepository;
    private final CarImageRepository carImageRepository;

    public List<CarInfoResponseDto> getAvailableCars() {
        return carsRepository.findAll().stream().filter(Car::isAvailable)
                .map(it -> CarInfoResponseDto.builder()
                        .id(it.getId())
                        .make(it.getMake())
                        .model(it.getModel())
                        .carType(it.getCarType())
                        .fuelConsumption(it.getFuelConsumption())
                        .fuelType(it.getFuelType())
                        .horsePower(it.getHorsePower())
                        .pricePerDay(it.getPricePerDay())
                        .available(it.isAvailable())
                        .build())
                .toList();
    }

    //ADMIN

    public List<CarInfoResponseDto> getAllCars() {
        if (!isAdmin()) {
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
                        .pricePerDay(it.getPricePerDay())
                        .available(it.isAvailable())
                        .build())
                .toList();
    }
    public void addCar(CarRequestDto request) {
        if (!isAdmin()) {
            throw new RuntimeException("You must be admin to do that");
        }

        var car = Car.builder()
                .make(request.getMake())
                .model(request.getModel())
                .carType(request.getCarType())
                .fuelConsumption(request.getFuelConsumption())
                .fuelType(request.getFuelType())
                .pricePerDay(request.getPricePerDay())
                .horsePower(request.getHorsePower())
                .available(false)
                .build();

        carsRepository.save(car);
    }

    public void modifyCar(CarRequestDto request, UUID cardId) {
        if (!isAdmin()) {
            throw new RuntimeException("You must be admin to do that");
        }

        var car = carsRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        car.setMake(request.getMake());
        car.setModel(request.getModel());
        car.setCarType(request.getCarType());
        car.setFuelConsumption(request.getFuelConsumption());
        car.setFuelType(request.getFuelType());
        car.setPricePerDay(request.getPricePerDay());
        car.setHorsePower(request.getHorsePower());

        carsRepository.save(car);
    }

    public void addCarImage(MultipartFile file, UUID carId) {
        if (!isAdmin()) {
            throw new RuntimeException("You must be admin to do that");
        }

        var car = carsRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        var carImage = CarImage.builder()
                .image(extractBytes(file))
                .car(car)
                .fileName(file.getOriginalFilename())
                .build();
        carImageRepository.save(carImage);
        car.addCarImage(carImage);
        carsRepository.save(car);
    }

    public void changeCarAvailability(UUID carId) {
        if(!isAdmin()){
            throw new RuntimeException("You must be admin to do that");
        }

        var car = carsRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        car.setAvailable(true);
        carsRepository.save(car);
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(i -> i.getAuthority().equals("ADMIN"));
    }

    private byte[] extractBytes(MultipartFile file) {
        try {
            return file.getBytes();
        }catch (IOException e){
            throw new RuntimeException("Error while extracting bytes from file");
        }
    }
}
