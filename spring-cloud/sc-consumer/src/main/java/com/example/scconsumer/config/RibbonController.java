package com.example.scconsumer.config;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RibbonController {

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 如果请求失败，会进入fallbackMethod这个方法
     * fallbackMethod这个方法要求参数和返回值与回调他的方法保持一致
     */
    @GetMapping("/ribbon/{wd}")
    @HystrixCommand(fallbackMethod = "fallbackMethod")
    public String sayHelloWorld(@PathVariable("wd") String parm) {
        String res = this.restTemplate.getForObject("http://sc-provider/test/" + parm, String.class);
        return res;
    }

    public String fallbackMethod(@PathVariable("wd") String parm) {
        return "fallback";
    }
}