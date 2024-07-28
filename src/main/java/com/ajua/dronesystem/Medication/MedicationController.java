package com.ajua.dronesystem.Medication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("medication")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationService medicationService;

    @PostMapping
    public ResponseEntity<Medication> addMedication(@RequestBody Medication medication) {
        return ResponseEntity.ok().body(medicationService.addMedication(medication));
    }
}
