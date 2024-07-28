package com.ajua.dronesystem.Drone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@RequestMapping
public interface DroneRepository extends JpaRepository<Drone, Long> {
    List<Drone> findAllByBatteryCapacityGreaterThanEqualAndWeightGreaterThan(Double batteryCapacity, Double weight);
    Optional<Drone> findBySerialNumber(String serialNumber);
}
