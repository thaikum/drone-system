package com.ajua.dronesystem.DroneLoad;

import com.ajua.dronesystem.Drone.Drone;
import com.ajua.dronesystem.MedicationItem.MedicationItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DroneLoad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Drone drone;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<MedicationItem> medicationItems;
    private Date loadingDate;
}
