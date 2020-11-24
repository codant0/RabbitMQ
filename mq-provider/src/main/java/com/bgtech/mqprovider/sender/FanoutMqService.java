package com.bgtech.mqprovider.sender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author HuangJF
 * @date 2020/11/23 0023 20:37
 */
@Service
public class FanoutMqService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendFanoutMessage(String message) {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = message;
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        rabbitTemplate.convertAndSend("fanoutExchange", null, map);
    }
}
