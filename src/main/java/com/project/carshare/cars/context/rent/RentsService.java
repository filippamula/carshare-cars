package com.project.carshare.cars.context.rent;

import com.project.carshare.cars.context.rent.dto.RentRequestDto;
import com.project.carshare.cars.domain.CarsRepository;
import com.project.carshare.cars.domain.Rent;
import com.project.carshare.cars.domain.RentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
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
        if(request.getDateFrom().isAfter(request.getDateTo())){
            throw new RuntimeException("Incorrect date of reservation");
        }
        var rents = rentRepository.findByCarId(carId)
                .stream().filter(it ->
                        !request.getDateFrom().isAfter(it.getDateTo()) && !request.getDateTo().isBefore(it.getDateFrom()))
                .toList();
        if(!rents.isEmpty()){
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

    @SuppressWarnings("unchecked")
    private UUID getUserId(){
        var id = ((Map<String,String>)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).get("userId");
        return UUID.fromString(id);
    }
}
