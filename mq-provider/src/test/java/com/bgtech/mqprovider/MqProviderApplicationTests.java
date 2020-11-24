package com.bgtech.mqprovider;

import com.bgtech.mqprovider.sender.DirectMqService;
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

    @Test
    public void sendMsgTest() {
        Map map = directMqService.sendMsg();
        System.out.println(map);
    }

}
