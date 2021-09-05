package com.bgtech.mqconsumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MqConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqConsumerApplication.class, args);
        log.info("MQ消费者服务启动成功");
    }

}
