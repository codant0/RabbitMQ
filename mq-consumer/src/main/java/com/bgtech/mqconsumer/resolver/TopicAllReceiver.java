package com.bgtech.mqconsumer.resolver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author HuangJF
 * @date 2020/11/23 0023 19:27
 * topic.woman 队列 绑定的主题交换机键值为topic.#
 */
@Component
@RabbitListener(queues = "topic.woman")
public class TopicAllReceiver {

    @RabbitHandler
    public void process(Map message) {
        System.out.println("woman receive topic message: " + message);
    }
}
