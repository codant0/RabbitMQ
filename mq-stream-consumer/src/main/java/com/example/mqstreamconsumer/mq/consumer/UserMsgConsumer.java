package com.example.mqstreamconsumer.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author HuangJF
 * @date 2021/4/22 0022 17:01
 */
@Component
@EnableBinding(UserMsgChannel.class)
@Slf4j
public class UserMsgConsumer {

    @StreamListener(value = UserMsgChannel.CONSUMER)
    public void listener(Message<String> entity) {
        log.info("接收到消息：" + entity.getPayload() + "当前时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
