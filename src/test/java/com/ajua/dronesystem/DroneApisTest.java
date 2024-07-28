package com.ajua.dronesystem;

import com.ajua.dronesystem.DTO.DroneLoadDTO;
import com.ajua.dronesystem.DTO.MedicationLoadDTO;
import com.ajua.dronesystem.Drone.Drone;
import com.ajua.dronesystem.Drone.DroneRepository;
import com.ajua.dronesystem.Medication.Medication;
import com.ajua.dronesystem.Medication.MedicationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class DroneApisTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private List<Drone> correctDroneList;
    private List<Drone> wrongDroleList;
    private List<Medication> medicationList;
    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    private MedicationRepository medicationRepository;


    @BeforeEach
    public void loadDronesFile() throws Exception {
        Resource resource = new ClassPathResource("initial-drones.json");
        correctDroneList = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
        });

        resource = new ClassPathResource("wrong-drone-list.json");
        wrongDroleList = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
        });

        resource = new ClassPathResource("initial_medication.json");
        medicationList = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
        });

        droneRepository.deleteAll();
        droneRepository.flush();
        medicationRepository.deleteAll();
        medicationRepository.flush();
    }

    @Test
    public void itShouldCheckSuccessfulDroneRegistration() throws Exception {
        for (Drone object : correctDroneList) {
            String jsonContent = objectMapper.writeValueAsString(object);

            mockMvc.perform(MockMvcRequestBuilders.post("/drone")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonContent))
                    .andExpect(status().isOk());
        }

        //Check if 3 drones were added.
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/drone")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var objectsList = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertThat(objectsList.size()).isEqualTo(3);
    }

    @Test
    public void itShouldCheckDronesAvailableForLoading() throws Exception {
//        droneRepository.deleteAll();
        droneRepository.saveAll(correctDroneList);

        //After adding drones, only 2 are above 25 %
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/drone/available-drones-for-loading")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var objectsList = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertThat(objectsList.size()).isEqualTo(2);
    }

    @Test
    public void itShouldCheckDroneLoadingFunctionality() throws Exception {
        List<Medication> medications = medicationRepository.saveAll(medicationList);
        droneRepository.saveAll(correctDroneList);
        System.out.println("Here: "+medications.get(0));
        DroneLoadDTO loadingData = new DroneLoadDTO(correctDroneList.get(0).getSerialNumber(), List.of(new MedicationLoadDTO(medications.get(0).getId(), 2)));
        mockMvc.perform(MockMvcRequestBuilders.post("/drone/load-drone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loadingData)))
                .andExpect(status().isOk());

        loadingData = new DroneLoadDTO(correctDroneList.get(0).getSerialNumber(), List.of(new MedicationLoadDTO(medications.get(0).getId(), 20)));
        mockMvc.perform(MockMvcRequestBuilders.post("/drone/load-drone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loadingData)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void itShouldCheckForIncorrectSerialNumber() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/drone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongDroleList.get(0))))
                .andExpect(status().isNotAcceptable())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).contains("Serial number must not exceed 100 characters");
    }

    @Test
    public void itShouldCheckDroneIncorrectWeight() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/drone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongDroleList.get(1))))
                .andExpect(status().isNotAcceptable())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).contains("Weight must not exceed 500gr");
    }

    @Test
    public void itShouldCheckIncorrectPercentage() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/drone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongDroleList.get(2))))
                .andExpect(status().isNotAcceptable())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).contains("Percentage must not exceed 100");
    }
}
