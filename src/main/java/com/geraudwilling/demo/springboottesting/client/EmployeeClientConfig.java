package com.geraudwilling.demo.springboottesting.client;

import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeeClientConfig {
    @Value("${feign.employee.url}")
    private  String url;

    @Bean
    public EmployeeClient employeeClient(){
        return Feign.builder()
                .encoder(new Encoder.Default())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(EmployeeClient.class))
                .logLevel(Logger.Level.FULL)
                .target(EmployeeClient.class, url);
    }
}
