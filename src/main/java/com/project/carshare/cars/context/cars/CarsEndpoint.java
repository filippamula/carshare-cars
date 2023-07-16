package com.project.carshare.cars.context.cars;

import com.project.carshare.cars.context.cars.dto.CarRequestDto;
import com.project.carshare.cars.context.cars.dto.CarInfoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("fleet")
@RequiredArgsConstructor
public class CarsEndpoint {

    private final CarsService carsService;

    @Operation(description = "Returns list of cars in offer")
    @GetMapping()
    public ResponseEntity<List<CarInfoResponseDto>> getAvailableCars(){
        return ResponseEntity.ok(carsService.getAvailableCars());
    }

    //ADMIN
    @Operation(description = "Returns list of all cars, requires admin role")
    @GetMapping("/all")
    public ResponseEntity<List<CarInfoResponseDto>> getAllCars() {
        return ResponseEntity.ok(carsService.getAllCars());
    }

    @Operation(description = "Adds a car requires admin role")
    @PostMapping("/add")
    public ResponseEntity<Void> addCar(@RequestBody CarRequestDto request) {
        carsService.addCar(request);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Modifies car, requires role admin")
    @PatchMapping("/{cardId}")
    public ResponseEntity<Void> modifyCar(
            @PathVariable UUID cardId,
            @RequestBody CarRequestDto request
    ) {
        carsService.modifyCar(request, cardId);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Adds an image to a car, requires role admin")
    @PostMapping("/{carId}/add-image")
    public ResponseEntity<Void> addCarImage(
            @PathVariable UUID carId,
            @RequestBody MultipartFile file
    ) {
        carsService.addCarImage(file, carId);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Changes car availability, requires role admin")
    @PostMapping("/{carId}/change-car-availability")
    public ResponseEntity<Void> addCarToFleet(@PathVariable UUID carId){
        carsService.changeCarAvailability(carId);
        return ResponseEntity.ok().build();
    }
}
