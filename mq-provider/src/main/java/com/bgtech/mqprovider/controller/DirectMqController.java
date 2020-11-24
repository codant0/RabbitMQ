package com.bgtech.mqprovider.controller;

import com.bgtech.mqprovider.sender.DirectMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author HuangJF
 * @date 2020/11/20 0020 19:43
 */
@RestController
@RequestMapping("/direct")
public class DirectMqController {

    @Autowired
    DirectMqService directMqService;

    @GetMapping("/sendMsg")
    public void sendMsg() {
        Map map = directMqService.sendMsg();
        System.out.println("map: " + map);
    }
}
