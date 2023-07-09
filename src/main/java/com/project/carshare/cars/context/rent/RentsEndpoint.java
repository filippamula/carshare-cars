package com.project.carshare.cars.context.rent;

import com.project.carshare.cars.context.rent.dto.RentRequestDto;
import com.project.carshare.cars.context.rent.dto.RentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/all")
    public ResponseEntity<List<RentResponseDto>> getRents() {
        return ResponseEntity.ok(rentsService.rents());
    }

    @PostMapping("/{rentId}/cancel")
    public ResponseEntity<Void> cancelRent(@PathVariable UUID rentId) {
        rentsService.cancelRent(rentId);
        return ResponseEntity.ok().build();
    }

    //ADMIN
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<RentResponseDto>> getUserRents(@PathVariable UUID userId) {
        return ResponseEntity.ok(rentsService.userRents(userId));
    }
}
