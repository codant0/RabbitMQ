package com.example.mqstreamconsumer.mq.consumer;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author HuangJF
 * @date 2021/4/22 0022 11:23
 */
public interface UserMsgChannel {

    String CONSUMER = "userMsg-consumer";

    @Input("userMsg-consumer")
    SubscribableChannel consumer();
}
