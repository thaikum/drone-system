package com.ajua.dronesystem.Drone;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Entity
@Data
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 100)
    @Length(max = 100, message = "Serial number must not exceed 100 characters")
    private String serialNumber;
    private DroneModels model;
    @Max(value = 500, message = "Weight must not exceed 500gr")
    private Double weight = 500.00;
    @Max(value = 100, message = "Percentage must not exceed 100")
    @Min(value = 0, message = "Value must be greater or equal to 0")
    private Double batteryCapacity = 100.00;
    private DroneState state;
}
