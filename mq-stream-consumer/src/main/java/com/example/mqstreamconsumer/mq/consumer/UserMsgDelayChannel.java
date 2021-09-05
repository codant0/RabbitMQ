package com.example.mqstreamconsumer.mq.consumer;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author HuangJF
 * @date 2021/4/22 0022 11:23
 */
public interface UserMsgDelayChannel {

    String CONSUMER = "userMsg-delay-consumer";

    @Input(CONSUMER)
    SubscribableChannel consumer();
}
