package com.ajua.dronesystem.Medication;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Data
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = "[\\w-]+", message = "Name can only contain letters, numbers, ‘-‘, ‘_’")
    private String name;
    private Double weight;
    @Pattern(regexp = "[A-Z_\\d]+", message = "Code can only contain upper case letters, underscore and numbers")
    private String code;
    @Lob
    private String image;
}
