package com.bgtech.mqconsumer.resolver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author HuangJF
 * @date 2020/11/20 0020 20:32
 */
@Component
@RabbitListener(queues = "TestDirectQueue")
public class DirectMsgReceiver {

    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("DirectReceiver消费者1收到消息  : " + testMessage.toString());
    }

}
