package com.ajua.dronesystem.DroneLoad;

import com.ajua.dronesystem.Drone.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneLoadRepository extends JpaRepository<DroneLoad, Long> {
    List<DroneLoad> findByDrone(Drone drone);
}
