package com.bgtech.mqprovider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MqProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqProviderApplication.class, args);
        log.info("MQ生产者服务启动成功");
    }

}
