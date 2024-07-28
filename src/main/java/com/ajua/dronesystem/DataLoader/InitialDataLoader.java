package com.ajua.dronesystem.DataLoader;

import com.ajua.dronesystem.Drone.Drone;
import com.ajua.dronesystem.Drone.DroneRepository;
import com.ajua.dronesystem.Medication.Medication;
import com.ajua.dronesystem.Medication.MedicationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitialDataLoader {
    private final ObjectMapper objectMapper;
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final Environment env;

    @PostConstruct
    @Profile("!test")
    public void loadInitialDrones() throws IOException {
        Resource resource = new ClassPathResource("initial-drones.json");
        List<Drone> drones = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
        });
        droneRepository.saveAll(drones);
    }

    @PostConstruct
    @Profile("!test")
    public void loadInitialMedications() throws IOException {
        Resource resource = new ClassPathResource("initial_medication.json");
        List<Medication> medications = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
        });
        medicationRepository.saveAll(medications);
    }
}
