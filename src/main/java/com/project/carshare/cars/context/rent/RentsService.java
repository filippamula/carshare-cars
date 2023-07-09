package com.project.carshare.cars.context.rent;

import com.project.carshare.cars.context.rent.dto.RentRequestDto;
import com.project.carshare.cars.context.rent.dto.RentResponseDto;
import com.project.carshare.cars.domain.CarsRepository;
import com.project.carshare.cars.domain.Rent;
import com.project.carshare.cars.domain.RentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentsService {

    private final RentsRepository rentRepository;
    private final CarsRepository carsRepository;

    public void rent(RentRequestDto request, UUID carId) {
        var userId = getUserId();
        var car = carsRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));
        if (request.getDateFrom().isBefore(LocalDate.now()) || request.getDateTo().isBefore(LocalDate.now())) {
            throw new RuntimeException("Incorrect date of reservation");
        }
        if (request.getDateFrom().isAfter(request.getDateTo())) {
            throw new RuntimeException("Incorrect date of reservation");
        }
        var rents = rentRepository.findByCarId(carId)
                .stream().filter(it ->
                        !request.getDateFrom().isAfter(it.getDateTo()) && !request.getDateTo().isBefore(it.getDateFrom()))
                .toList();
        if (!rents.isEmpty()) {
            throw new RuntimeException("Car already taken");
        }

        var rent = Rent.builder()
                .dateFrom(request.getDateFrom())
                .dateTo(request.getDateTo())
                .amountToPay(BigDecimal.valueOf(100)) //TODO impl
                .userId(userId)
                .car(car)
                .build();

        rentRepository.save(rent);
    }

    public List<RentResponseDto> rents() {
        var userId = getUserId();

        return rentRepository.findRentsByUserId(userId)
                .stream().map(it -> RentResponseDto.builder()
                        .id(it.getId())
                        .dateFrom(it.getDateFrom())
                        .dateTo(it.getDateTo())
                        .amountToPay(it.getAmountToPay())
                        .userId(it.getUserId())
                        .carId(it.getId())
                        .build())
                .toList();
    }

    public void cancelRent(UUID rentId) {
        var userId = getUserId();

        var rent = rentRepository.findRentById(rentId)
                .orElseThrow(() -> new RuntimeException("Rent not found"));
        if (!rent.getUserId().equals(userId) && !isAdmin()) {
            throw new RuntimeException("Rent is not user's rent");
        }
        if (rent.getDateFrom().isBefore(LocalDate.now())) {
            throw new RuntimeException("You can't cancel rent that already started");
        }

        rentRepository.delete(rent);
    }

    public List<RentResponseDto> userRents(UUID userId) {
        if(!isAdmin()){
            throw new RuntimeException("You must be admin to do that");
        }
        return rentRepository.findRentsByUserId(userId)
                .stream().filter(it -> it.getUserId().equals(userId))
                .map(it -> RentResponseDto.builder()
                        .id(it.getId())
                        .dateFrom(it.getDateFrom())
                        .dateTo(it.getDateTo())
                        .amountToPay(it.getAmountToPay())
                        .userId(it.getUserId())
                        .carId(it.getId())
                        .build())
                .toList();
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(i -> i.getAuthority().equals("ADMIN"));
    }

    private UUID getUserId() {
        var id = SecurityContextHolder.getContext().getAuthentication().getName();
        return UUID.fromString(id);
    }
}
