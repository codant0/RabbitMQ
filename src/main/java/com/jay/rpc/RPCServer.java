package com.jay.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author HuangJF
 * @date 2021/5/23 0023 15:21
 */
public class RPCServer {
    public static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("193.112.82.144");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        // 创建连接
        Connection connection = factory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
        channel.basicQos(1);
        System.out.println(" [x] Awaiting RPC requests");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(properties.getCorrelationId())
                        .build();
                String response = "";
                try {
                    String message = new String(body, StandardCharsets.UTF_8);
                    int n = Integer.parseInt(message);
                    System.out.println(" [.] fib(" + message + ")");
                    response += fib(n);
                } catch (Exception e) {
                    System.out.println(" [.] " + e.toString());
                } finally {
                    channel.basicPublish("", properties.getReplyTo(),
                            replyProps, response.getBytes(StandardCharsets.UTF_8));
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

    }

    private static int fib(int n ) {
        return n == 0 ? 0 : fib(n - 1) + fib(n - 2);
    }
}
