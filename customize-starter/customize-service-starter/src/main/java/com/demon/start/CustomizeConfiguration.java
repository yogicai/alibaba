package com.demon.start;

import com.demon.add.AddServiceImpl;
import com.demon.minus.MinusServiceNotSupportNegativeImpl;
import com.demon.minus.MinusServiceSupportNegativeImpl;
import com.demon.service.AddService;
import com.demon.service.MinusService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CustomizeConfiguration {

    @Bean
    public AddService getAddService() {
        System.out.println("create addService");
        return new AddServiceImpl();
    }

    /**
     * 如果配置了com.alibaba.supportnegative=true，
     * 就实例化MinusServiceSupportNegativeImpl
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "com.alibaba", name = "supportnegative", havingValue = "true")
    public MinusService getSupportMinusService() {
        System.out.println("create minusService support minus");
        return new MinusServiceSupportNegativeImpl();
    }

    /**
     * 如果没有配置com.alibaba.supportnegative=true，
     * 就不会实例化MinusServiceSupportNegativeImpl，
     * 这里的条件是如果没有MinusService类型的bean，就在此实例化一个
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(MinusService.class)
    public MinusService getNotSupportMinusService() {
        System.out.println("create minusService not support minus");
        return new MinusServiceNotSupportNegativeImpl();
    }
}