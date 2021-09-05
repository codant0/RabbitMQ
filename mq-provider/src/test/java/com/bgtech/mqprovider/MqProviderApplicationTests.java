package com.bgtech.mqprovider;

import com.bgtech.mqprovider.sender.DirectMqService;
import com.bgtech.mqprovider.sender.FanoutMqService;
import com.bgtech.mqprovider.sender.TopicMqService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
class MqProviderApplicationTests {

    @Autowired
    DirectMqService directMqService;

    @Autowired
    TopicMqService topicMqService;

    @Autowired
    FanoutMqService fanoutMqService;

    @Test
    public void sendDirectMsg() {
        Map map = directMqService.sendMsg();
        System.out.println(map);
    }

    @Test
    public void sendTopicMsg() {
        topicMqService.sendManTopicMessage("message to man topic (woman is bound to all topic, man is bound to only man)");
        topicMqService.sendAllTopicMessage("message to woman topic (woman is bound to all topic, man is bound to only man)");
    }

    @Test
    public void sendFanoutMsg() throws InterruptedException {
        fanoutMqService.sendFanoutMessage("message to fanout exchange");
        //Thread.sleep(2000);
    }

}
