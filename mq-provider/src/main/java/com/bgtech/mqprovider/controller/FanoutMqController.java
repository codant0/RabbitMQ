package com.bgtech.mqprovider.controller;

import com.bgtech.mqprovider.sender.FanoutMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuangJF
 * @date 2020/11/23 0023 20:39
 */
@RestController
@RequestMapping("/fanout")
public class FanoutMqController {

    @Autowired
    FanoutMqService fanoutMqService;

    @GetMapping("/sendMsg")
    public void sendMsg(@RequestParam("msg") String message) {
        fanoutMqService.sendFanoutMessage(message);
    }
}
