package com.example.mqstreamproducer.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author HuangJF
 * @date 2021/4/23 0023 11:48
 */
@Slf4j
@Component
@EnableBinding(UserMsgDelayChannel.class)
public class UserMsgDelaySender {

    @Autowired
    UserMsgDelayChannel userMsgDelayChannel;

    public Boolean send(String msg) {
        log.info("消息发送：" + msg + ",当前时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Message<String> message = MessageBuilder.withPayload(msg).build();
        return userMsgDelayChannel.producer().send(message);
    }

    public Boolean sendDelay(String msg, Long delay) {
        log.info("消息发送：" + msg + ",当前时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Message<String> message = MessageBuilder.withPayload(msg).setHeader("x-delay", delay).build();
        return userMsgDelayChannel.producer().send(message);
    }
}
