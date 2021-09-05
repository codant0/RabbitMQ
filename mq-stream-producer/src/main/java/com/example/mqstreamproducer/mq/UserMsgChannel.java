package com.example.mqstreamproducer.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author HuangJF
 * @date 2021/4/22 0022 19:31
 */
public interface UserMsgChannel {

    @Output("user-msg-producer")
    MessageChannel producer();
}
