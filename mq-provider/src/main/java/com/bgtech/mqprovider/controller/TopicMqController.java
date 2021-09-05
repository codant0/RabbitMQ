package com.bgtech.mqprovider.controller;

import com.bgtech.mqprovider.sender.TopicMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuangJF
 * @date 2020/11/23 0023 19:22
 */
@RestController
@RequestMapping("/topic")
public class TopicMqController {

    @Autowired
    TopicMqService topicMqService;

    @GetMapping("/sendMsg1")
    public void sendTopMsg1(@RequestParam("msg")String message) {
        topicMqService.sendManTopicMessage(message);
    }

    @GetMapping("/sendMsg2")
    public void sendTopMsg2(@RequestParam("msg")String message) {
        topicMqService.sendAllTopicMessage(message);
    }
}
