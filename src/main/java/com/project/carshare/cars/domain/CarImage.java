package com.project.carshare.cars.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CAR_IMAGES")
@Entity
@Data
@Builder
public class CarImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private byte[] image;
    private String fileName;
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
}
