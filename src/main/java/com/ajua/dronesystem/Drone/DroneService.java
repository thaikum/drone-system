package com.ajua.dronesystem.Drone;

import com.ajua.dronesystem.DTO.MedicationLoadDTO;
import com.ajua.dronesystem.EventLog.EventLogService;
import com.ajua.dronesystem.DTO.DroneLoadDTO;
import com.ajua.dronesystem.DTO.MedicationItemDTO;
import com.ajua.dronesystem.DroneLoad.DroneLoad;
import com.ajua.dronesystem.DroneLoad.DroneLoadRepository;
import com.ajua.dronesystem.Medication.Medication;
import com.ajua.dronesystem.Medication.MedicationRepository;
import com.ajua.dronesystem.MedicationItem.MedicationItem;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DroneService {
    private final DroneRepository droneRepository;
    private final DroneLoadRepository droneLoadRepository;
    private final ResourceLoader resourceLoader;
    private final EventLogService eventLogService;
    private final MedicationRepository medicationRepository;

    public Drone registerDrone(Drone drone) {
        return droneRepository.save(drone);
    }

    public Double checkBattery(String serialNumber) {
        return droneRepository.findBySerialNumber(serialNumber).orElseThrow().getBatteryCapacity();
    }

    public Set<MedicationItemDTO> checkDroneMedication(String serialNumber) {
        Drone drone = droneRepository.findBySerialNumber(serialNumber).orElseThrow();
        Set<MedicationItemDTO> medicationItemDTOs = new HashSet<>();
        droneLoadRepository.findByDrone(drone)
                .forEach(droneLoad -> {
                    Set<MedicationItemDTO> loadMedicationItemDTOs = new HashSet<>();
                    droneLoad.getMedicationItems().forEach(droneLoadMedicationItem -> {
                        loadMedicationItemDTOs.add(new MedicationItemDTO(droneLoadMedicationItem.getMedication(), droneLoadMedicationItem.getQuantity()));
                    });
                    medicationItemDTOs.addAll(loadMedicationItemDTOs);
                });
        return medicationItemDTOs;
    }

    public List<Drone> dronesAvailableForLoading() {
        return droneRepository.findAllByBatteryCapacityGreaterThanEqualAndWeightGreaterThan(25.0, 0.0);
    }

    @Scheduled(fixedRate = 5000)
    public void checkDroneBattery() {
        List<Drone> drones = droneRepository.findAll();
        Map<Boolean, List<Drone>> droneMap = drones.stream().collect(Collectors.partitioningBy(v -> v.getBatteryCapacity() > 25));
        String action = String.format("Checked battery. %d drone/s are above 25%% while %d are below 25%%", droneMap.get(true).size(), droneMap.get(false).size());
        eventLogService.createAudit(action, "System");

        drones.forEach(drone -> {
            if(drone.getBatteryCapacity() < 25) {
                drone.setState(DroneState.IDLE);
            }
        });
    }

    @Transactional
    public void loadDrones(DroneLoadDTO droneLoadDTO) {
        Drone drone = droneRepository.findBySerialNumber(droneLoadDTO.droneSerial()).orElseThrow();

        if (drone.getBatteryCapacity() < 25) {
            throw new RuntimeException("Cannot load medication to a drone with battery capacity < 25");
        }

        Double droneAvailableCapacity = drone.getWeight();
        double totalLoadWeight = 0.0;

        Set<MedicationItem> medicationItemSet = new HashSet<>();
        for (MedicationLoadDTO medicationLoadDTO : droneLoadDTO.medications()) {
            Medication medication = medicationRepository.findById(medicationLoadDTO.id()).orElseThrow();
            var medicationItem = MedicationItem.builder()
                    .quantity(medicationLoadDTO.quantity())
                    .medication(medication)
                    .build();
            medicationItemSet.add(medicationItem);
            totalLoadWeight += medication.getWeight() * medicationLoadDTO.quantity();
        }

        DroneLoad droneLoad = DroneLoad.builder()
                .drone(drone)
                .loadingDate(new Date())
                .medicationItems(medicationItemSet)
                .build();

        if (totalLoadWeight > droneAvailableCapacity) {
            throw new RuntimeException("Load exceeds drone's capacity");
        }

        droneLoadRepository.save(droneLoad);
        drone.setWeight(droneAvailableCapacity - totalLoadWeight);
        drone.setState(DroneState.LOADED);
        droneRepository.save(drone);
    }
}
