package com.ajua.dronesystem.Medication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicationService {
    private final MedicationRepository medicationRepository;

    public Medication addMedication(Medication medication) {
        return medicationRepository.save(medication);
    }
}
