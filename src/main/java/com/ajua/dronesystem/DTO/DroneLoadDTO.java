package com.ajua.dronesystem.DTO;

import com.ajua.dronesystem.Drone.Drone;

import java.util.List;

public record DroneLoadDTO(String droneSerial, List<MedicationLoadDTO> medications) {
}
