package com.ajua.dronesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DroneSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DroneSystemApplication.class, args);
    }

}
