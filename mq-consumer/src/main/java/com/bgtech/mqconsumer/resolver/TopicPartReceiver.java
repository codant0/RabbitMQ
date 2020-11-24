package com.bgtech.mqconsumer.resolver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author HuangJF
 * @date 2020/11/23 0023 19:27
 */
@Component
@RabbitListener(queues = "topic.part")
public class TopicPartReceiver {

    @RabbitHandler
    public void process(Map message) {
        System.out.println("part topic message: " + message);
    }
}
