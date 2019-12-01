package com.example.scconsumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * name是指要请求的服务名称。这里请求的是服务提供者
 * fallback 是指请求失败，进入断路器的类，和使用ribbon是一样的。
 * configuration 是feign的一些配置，例如编码器等
 */
@FeignClient(name = "sc-provider",fallback = MFeignClientFallback.class, configuration = MFeignConfig.class)
public interface MFeignClient {

    @GetMapping(value = "/test/{msg}")
    String sayHelloWorld(@PathVariable("msg") String msg);

    @GetMapping(value = "/test/list")
    List<Integer> list();

    @GetMapping(value = "/test/list")
    Integer[] array();
}