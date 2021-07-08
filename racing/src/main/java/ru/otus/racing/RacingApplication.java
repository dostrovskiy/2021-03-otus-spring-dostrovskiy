package ru.otus.racing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableIntegration
@IntegrationComponentScan
@SpringBootApplication
public class RacingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RacingApplication.class, args);
    }

}
