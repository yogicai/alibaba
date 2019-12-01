package com.example.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.util.Scanner;

@EnableEurekaClient
@EnableWebMvc
@RestController
@SpringBootApplication
public class ProviderApplication {

    @RequestMapping(value = "/call/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String home(@PathVariable("id") String id, HttpServletRequest request) {
        return "Hello world: " + request.getRequestURL() + " " + id;
    }

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String port = scan.nextLine();
        new SpringApplicationBuilder(ProviderApplication.class).properties("server.port="+port).run(args);

//        SpringApplication.run(ProviderApplication.class, args);
    }

}
