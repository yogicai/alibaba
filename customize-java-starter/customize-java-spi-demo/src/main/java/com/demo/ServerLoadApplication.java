package com.demo;

import com.demo.spi.DriverService;

import java.util.ServiceLoader;

/**
 * @author: yogiCai
 * @create: 2019-12-01 12:49
 **/
public class ServerLoadApplication {

    public static void main(String[] args) {
        ServiceLoader<DriverService> serviceLoader = ServiceLoader.load(DriverService.class);
        for (DriverService driverService: serviceLoader){
            System.out.println(driverService.getName());
        }
    }

}
