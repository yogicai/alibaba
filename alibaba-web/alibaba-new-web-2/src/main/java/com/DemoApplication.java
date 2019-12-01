package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@MapperScan("com.example.mapper")
@SpringBootApplication(scanBasePackages={"com.example","com.mom.redis"})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    public static void main1(String[] args) {
        new SpringApplicationBuilder(DemoApplication.class).profiles("spring.config.location=classpath:/abc.properties").run(args);
    }

}
