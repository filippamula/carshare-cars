package com.project.carshare.cars.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RENTS")
@Entity
@Data
@Builder
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private BigDecimal amountToPay;
    private UUID userId;
    @ManyToOne()
    @JoinColumn(name = "car_id")
    private Car car;
}
