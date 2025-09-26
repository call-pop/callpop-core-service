package com.sdcompany.callpop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class CallPopCoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CallPopCoreServiceApplication.class, args);
    }
}
