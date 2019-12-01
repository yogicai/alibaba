package com.example.sczipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.internal.EnableZipkinServer;

@EnableZipkinServer
@SpringBootApplication
public class ScZipkinApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScZipkinApplication.class, args);
    }

}
