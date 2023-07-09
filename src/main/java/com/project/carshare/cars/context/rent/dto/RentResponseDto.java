package com.project.carshare.cars.context.rent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentResponseDto {

    private UUID id;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private BigDecimal amountToPay;
    private UUID userId;
    private UUID carId;
}
