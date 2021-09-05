package com.example.mqstreamproducer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MqStreamProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqStreamProducerApplication.class, args);
        log.info("mq-stream-producer服务启动成功！");
    }

}
