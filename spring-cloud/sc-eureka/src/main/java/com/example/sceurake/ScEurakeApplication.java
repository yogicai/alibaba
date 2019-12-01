package com.example.sceurake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ScEurakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScEurakeApplication.class, args);
    }

}
