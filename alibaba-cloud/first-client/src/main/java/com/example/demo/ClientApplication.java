package com.example.demo;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.shared.Applications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Controller
@EnableEurekaClient
@SpringBootApplication
public class ClientApplication {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 服务注入发现客户端
     */
    @Autowired
    private DiscoveryClient discoveryClient;


    @GetMapping(value = "router")
    @ResponseBody
    public String doOtherStuff() {
        String results = restTemplate.getForObject("http://first-provider/call/1", String.class);
        return results;
    }

    @GetMapping(value = "router")
    @ResponseBody
    public String serviceCount() {
        Applications applications = discoveryClient.getApplications();
        applications.getRegisteredApplications().forEach(app -> {
            app.getInstances().forEach(instanceInfo -> {
                System.out.println(instanceInfo.getHostName());
            });
        });
        return "success";
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

}
