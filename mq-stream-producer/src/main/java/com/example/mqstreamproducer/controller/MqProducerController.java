package com.example.mqstreamproducer.controller;

import com.example.mqstreamproducer.mq.UserMsgDelaySender;
import com.example.mqstreamproducer.mq.UserMsgSender;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuangJF
 * @date 2021/4/22 0022 19:36
 */
@Slf4j
@RestController
public class MqProducerController {

    @Autowired
    UserMsgSender userMsgSender;

    @Autowired
    UserMsgDelaySender userMsgDelaySender;

    @ApiOperation("发送消息")
    @GetMapping("/api/mq/send")
    public boolean sendMsg(@RequestParam("msg")String msg) {
        return userMsgSender.send(msg);
    }

    @ApiOperation("发送延迟消息")
    @GetMapping("/api/mq/delay/send")
    public boolean sendDelayMsg(@RequestParam("msg")String msg, @RequestParam("delay")Long delay) {
        return userMsgDelaySender.sendDelay(msg, delay);
    }
}
