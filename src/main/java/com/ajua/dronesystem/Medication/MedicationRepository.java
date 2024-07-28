package com.ajua.dronesystem.Medication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
