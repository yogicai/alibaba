package com.debug.mooc.dubbo.two.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

//import org.springframework.boot.context.web.SpringBootServletInitializer; //1.x版本

/**
 * @Author:debug (SteadyJack)
 * @Date: 2019/1/13 15:06
 **/
@SpringBootApplication
//@ImportResource(value = {"classpath:spring/spring-jdbc.xml","classpath:spring/spring-dubbo.xml"})
@ImportResource(value = {"classpath:spring/spring-jdbc.xml"})
@MapperScan(basePackages = "com.debug.mooc.dubbo.two.model.mapper")
public class BootTwoApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BootTwoApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(BootTwoApplication.class, args);
    }

}