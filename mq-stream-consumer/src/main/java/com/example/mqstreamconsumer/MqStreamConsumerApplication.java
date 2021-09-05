package com.example.mqstreamconsumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MqStreamConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqStreamConsumerApplication.class, args);
        log.info("mq-stream-consumer服务启动成功！");
    }

}
