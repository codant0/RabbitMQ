package com.jay.rpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;

import java.io.IOException;
import java.sql.Connection;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author HuangJF
 * @date 2021/5/23 0023 15:36
 */
public class RPCClient {

    private Connection connection;
    private Channel channel;
    private final String requestQueueName = "rpc_queue";
    private String replyQueueName;
    private DefaultConsumer consumer;

    public RPCClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("193.112.82.144");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        // 创建连接
        com.rabbitmq.client.Connection connection = factory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        replyQueueName = channel.queueDeclare().getQueue();
        consumer = new DefaultConsumer(channel);
        channel.basicConsume(replyQueueName, true, consumer);
    }

    public String call(String message) throws IOException {
        String response = null;
        String corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();
        channel.basicPublish("", requestQueueName, props, message.getBytes());
        while (true) {
            
        }
    }
}
