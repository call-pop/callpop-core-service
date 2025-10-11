package com.sdcompany.tadak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class TadakAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TadakAppBackendApplication.class, args);
    }
}
