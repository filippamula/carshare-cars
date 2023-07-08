package com.project.carshare.cars.context.rent;

import com.project.carshare.cars.context.rent.dto.RentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/cars/rent")
@RequiredArgsConstructor
public class RentsEndpoint {

    private final RentsService rentsService;

    @PostMapping("/{carId}")
    public ResponseEntity<Void> rent(
            @PathVariable UUID carId,
            @RequestBody RentRequestDto request) {
        rentsService.rent(request, carId);
        return ResponseEntity.ok().build();
    }
}
