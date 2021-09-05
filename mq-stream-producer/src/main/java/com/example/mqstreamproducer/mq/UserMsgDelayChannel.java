package com.example.mqstreamproducer.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author HuangJF
 * @date 2021/4/23 0023 11:48
 */
public interface UserMsgDelayChannel {

    @Output("user-msg-delay-producer")
    MessageChannel producer();
}
