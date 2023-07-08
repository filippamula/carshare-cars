package com.project.carshare.cars.context.cars;

import com.project.carshare.cars.context.cars.dto.CarRequestDto;
import com.project.carshare.cars.context.cars.dto.CarInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/cars/fleet")
@RequiredArgsConstructor
public class CarsEndpoint {

    private final CarsService carsService;

    @GetMapping("/all")
    public ResponseEntity<List<CarInfoResponseDto>> getAllCars() {
        return ResponseEntity.ok(carsService.getAllCars());
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addCar(@RequestBody CarRequestDto request) {
        carsService.addCar(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{cardId}")
    public ResponseEntity<Void> modifyCar(
            @PathVariable UUID cardId,
            @RequestBody CarRequestDto request
    ) {
        carsService.modifyCar(request, cardId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{carId}/add-image")
    public ResponseEntity<Void> addCarImage(
            @PathVariable UUID carId,
            @RequestBody MultipartFile file
    ) {
        carsService.addCarImage(file, carId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{carId}/change-car-availability")
    public ResponseEntity<Void> addCarToFleet(@PathVariable UUID carId){
        carsService.changeCarAvailability(carId);
        return ResponseEntity.ok().build();
    }
}
