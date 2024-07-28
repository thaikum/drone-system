package com.ajua.dronesystem.Drone;

import com.ajua.dronesystem.DTO.DroneLoadDTO;
import com.ajua.dronesystem.DTO.MedicationItemDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("drone")
@RequiredArgsConstructor
public class DroneController {
    private final DroneService droneService;
    private final DroneRepository droneRepository;

    @PostMapping
    public ResponseEntity<Drone> createDrone(@RequestBody Drone drone) {
        return ResponseEntity.ok().body(droneService.registerDrone(drone));
    }

    @GetMapping
    public ResponseEntity<List<Drone>> getAllDrones() {
        return ResponseEntity.ok(droneRepository.findAll());
    }

    @PostMapping("load-drone")
    public ResponseEntity<String> loadDrone(@RequestBody DroneLoadDTO droneLoadDTO) {
        droneService.loadDrones(droneLoadDTO);
        return ResponseEntity.ok().body("Drone loaded successfully");
    }

    @GetMapping("available-drones-for-loading")
    public ResponseEntity<List<Drone>> availableDronesForLoading() {
        return ResponseEntity.ok().body(droneService.dronesAvailableForLoading());
    }

    @GetMapping("check-battery-level/{serialNumber}")
    public ResponseEntity<Double> checkDroneBatteryLevel(@PathVariable String serialNumber) {
        return ResponseEntity.ok().body(droneService.checkBattery(serialNumber));
    }

    @GetMapping("items-loaded/{serialNumber}")
    public ResponseEntity<Set<MedicationItemDTO>> itemsLoaded(@PathVariable String serialNumber) {
        return ResponseEntity.ok(droneService.checkDroneMedication(serialNumber));
    }
}
