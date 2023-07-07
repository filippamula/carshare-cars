package com.project.carshare.cars.context.cars;

import com.project.carshare.cars.context.cars.dto.AddCarRequestDto;
import com.project.carshare.cars.context.cars.dto.CarInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/cars/fleet")
@RequiredArgsConstructor
public class CarsEndpoint {

    private final CarsService carsService;

    @GetMapping("/all")
    public ResponseEntity<List<CarInfoResponseDto>> getAllCars(){
        return ResponseEntity.ok(carsService.getAllCars());
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addCar(@RequestBody AddCarRequestDto request){
        carsService.addCar(request);
        return ResponseEntity.ok().build();
    }
}
