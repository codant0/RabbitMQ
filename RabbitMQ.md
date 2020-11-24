# RabbitMQ学习笔记

## 安装

* Docker安装

  > ``` shell
  > # management版本才带有管理界面，通过ip:15672访问
  > docker pull rabbitmq:management
  > 
  > docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq rabbitmq:management
  > ```

* 访问test : http://ip:15672
  * 默认帐号: guest
  * 默认密码: guest

## 依赖

> ``` xml
> <!--rabbitmq-->
> <dependency>
>     <groupId>org.springframework.boot</groupId>
>     <artifactId>spring-boot-starter-amqp</artifactId>
> </dependency>
> <dependency>
>     <groupId>org.springframework.boot</groupId>
>     <artifactId>spring-boot-starter-web</artifactId>
> </dependency>
> ```
>
> 

## 配置

> ``` yaml
> server:
>   port: 8021
> spring:
>   #给项目来个名字
>   application:
>     name: rabbitmq-provider
>   #配置rabbitMq 服务器
>   rabbitmq:
>     host: 127.0.0.1
>     port: 5672
>     username: guest
>     password: guest
>     #虚拟host 可以不设置,使用server默认host
>     virtual-host: host
> 
> ```
>
> 

## 基本概念

### ConnectionFactory、Connection、Channel

* ConnectionFactory、Connection、Channel都是RabbitMQ对外提供的API中最基本的对象。Connection是RabbitMQ的socket链接，它封装了socket协议相关部分逻辑。ConnectionFactory为Connection的制造工厂。 Channel是我们与RabbitMQ打交道的最重要的一个接口，我们大部分的业务操作是在Channel这个接口中完成的，包括定义Queue、定义Exchange、绑定Queue与Exchange、发布消息等。

### Queue

* Queue（队列）是RabbitMQ的内部对象，用于存储消息。RabbitMQ中的消息都只能存储在Queue中，生产者（下图中的P）生产消息并最终投递到Queue中，消费者（下图中的C）可以从Queue中获取消息并消费。
* <img src="https://www.liangzl.com/editorImages/cawler/20180628181325_899.jpg" alt="img" style="zoom: 80%;" />





## 三种消息推送/接收模式（对应三种交换机）

### 直连型交换机（Direct Exchange ）

* 直连型交换机，根据消息携带的路由键将消息投递给对应队列。

  大致流程，有一个队列绑定到一个直连交换机上，同时赋予一个路由键 routing key 。然后当一个消息携带着路由值为X，这个消息通过生产者发送给交换机时，交换机就会根据这个路由值X去寻找绑定值也是X的队列。

* ==**生产者**==

> ``` java
> package com.bgtech.mqprovider.config;
> 
> import org.springframework.context.annotation.Configuration;
> import org.springframework.amqp.core.Binding;
> import org.springframework.amqp.core.BindingBuilder;
> import org.springframework.amqp.core.DirectExchange;
> import org.springframework.amqp.core.Queue;
> import org.springframework.context.annotation.Bean;
> /**
>  * @author HuangJF
>  * @date 2020/11/20 0020 17:05
>  */
> @Configuration
> public class DirectRabbitConfig {
> 
>     // 队列 起名：TestDirectQueue
>     @Bean
>     public Queue TestDirectQueue() {
>         // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
>         // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
>         // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
>         //   return new Queue("TestDirectQueue",true,true,false);
> 
>         // 一般设置一下队列的持久化就好,其余两个就是默认false
>         return new Queue("TestDirectQueue",true);
>     }
> 
>     // Direct交换机 起名：TestDirectExchange
>     @Bean
>     DirectExchange TestDirectExchange() {
>         //  return new DirectExchange("TestDirectExchange",true,true);
>         return new DirectExchange("TestDirectExchange",true,false);
>     }
> 
>     // 绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
>     @Bean
>     Binding bindingDirect() {
>         return BindingBuilder.bind(TestDirectQueue()).to(TestDirectExchange()).with("TestDirectRouting");
>     }
>     
>     @Bean
>     DirectExchange lonelyDirectExchange() {
>         return new DirectExchange("lonelyDirectExchange");
>     }
> }
> 
> ```
>
> config

> ``` java
> package com.bgtech.mqprovider.sender;
> 
> import org.junit.Test;
> import org.springframework.amqp.rabbit.core.RabbitTemplate;
> import org.springframework.beans.factory.annotation.Autowired;
> import org.springframework.stereotype.Component;
> 
> import java.time.LocalDateTime;
> import java.time.format.DateTimeFormatter;
> import java.util.HashMap;
> import java.util.Map;
> import java.util.UUID;
> 
> /**
>  * @author HuangJF
>  * @date 2020/11/20 0020 19:37
>  */
> @Component
> public class MqSenderService {
> 
>     @Autowired
>     RabbitTemplate rabbitTemplate;
> 
>     public Map sendMsg() {
>         String messageId = String.valueOf(UUID.randomUUID());
>         String messageData = "test message, hello!";
>         String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
>         Map<String,Object> map=new HashMap<>();
>         map.put("messageId",messageId);
>         map.put("messageData",messageData);
>         map.put("createTime",createTime);
>         //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
>         rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
>         return map;
>     }
> }
> ```
>
> 

* ==**消费者**==

> ``` java
> @Component
> //监听的队列名称 TestDirectQueue
> @RabbitListener(queues = "TestDirectQueue")
> public class MsgReceiver {
> 
>     @RabbitHandler
>     public void process(Map testMessage) {
>         System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
>     }
> }
> 
> // config
> @Configuration
> public class DirectRabbitConfig {
> 
>     //队列 起名：TestDirectQueue
>     @Bean
>     public Queue TestDirectQueue() {
>         return new Queue("TestDirectQueue",true);
>     }
> 
>     //Direct交换机 起名：TestDirectExchange
>     @Bean
>     DirectExchange TestDirectExchange() {
>         return new DirectExchange("TestDirectExchange");
>     }
> 
>     //绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
>     @Bean
>     Binding bindingDirect() {
>         return BindingBuilder.bind(TestDirectQueue()).to(TestDirectExchange()).with("TestDirectRouting");
>     }
> }
> ```
>
> 消费者单纯的使用，其实可以不用添加config配置，直接建监听就好，使用注解来让监听器监听对应的队列即可。配置上了的话，其实消费者也是生成者的身份，也能推送该消息。

* 直连模式下多个监听者，同时消费一个生产者，会以轮询的方式进行，且不会重复消费。

### 扇形交换机（Fanout Exchange）

* 扇形交换机没有路由建概念，绑定了路由键也不生效。交换机在接收到消息后，会直接转发到绑定到它上面的所有队列。

* ==**生产者**==

  * > ``` java
    > // config
    > @Configuration
    > public class FanoutRabbitConfig {
    >     /**
    >      *  创建三个队列 ：fanout.A   fanout.B  fanout.C
    >      *  将三个队列都绑定在交换机 fanoutExchange 上
    >      *  因为是扇型交换机, 路由键无需配置,配置也不起作用
    >      */
    > 
    > 
    >     @Bean
    >     public Queue queueA() {
    >         return new Queue("fanout.A");
    >     }
    > 
    >     @Bean
    >     public Queue queueB() {
    >         return new Queue("fanout.B");
    >     }
    > 
    >     @Bean
    >     public Queue queueC() {
    >         return new Queue("fanout.C");
    >     }
    > 
    >     @Bean
    >     FanoutExchange fanoutExchange() {
    >         return new FanoutExchange("fanoutExchange");
    >     }
    > 
    >     @Bean
    >     Binding bindingExchangeA() {
    >         return BindingBuilder.bind(queueA()).to(fanoutExchange());
    >     }
    > 
    >     @Bean
    >     Binding bindingExchangeB() {
    >         return BindingBuilder.bind(queueB()).to(fanoutExchange());
    >     }
    > 
    >     @Bean
    >     Binding bindingExchangeC() {
    >         return BindingBuilder.bind(queueC()).to(fanoutExchange());
    >     }
    > 
    > }
    > ```

  * > ``` java
    > @Service
    > public class FanoutMqService {
    > 
    >     @Autowired
    >     RabbitTemplate rabbitTemplate;
    > 
    >     public void sendFanoutMessage(String message) {
    >         String messageId = String.valueOf(UUID.randomUUID());
    >         String messageData = message;
    >         String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    >         Map<String, Object> map = new HashMap<>();
    >         map.put("messageId", messageId);
    >         map.put("messageData", messageData);
    >         map.put("createTime", createTime);
    >         rabbitTemplate.convertAndSend("fanoutExchange", null, map);
    >     }
    > }
    > ```

* ==**消费者**==

  * > ``` java
    > // config，消费者单纯的使用，其实可以不用添加这个配置，直接建后面的监听就好，使用注解来让监听器监听对应的队列即可。配置上了的话，其实消费者也是生成者的身份，也能推送该消息
    > @Configuration
    > public class FanoutRabbitConfig {
    > 
    >     /**
    >      *  创建三个队列 ：fanout.A   fanout.B  fanout.C
    >      *  将三个队列都绑定在交换机 fanoutExchange 上
    >      *  因为是扇型交换机, 路由键无需配置,配置也不起作用
    >      */
    > 
    >     @Bean
    >     public Queue queueA() {
    >         return new Queue("fanout.A");
    >     }
    > 
    >     @Bean
    >     public Queue queueB() {
    >         return new Queue("fanout.B");
    >     }
    > 
    >     @Bean
    >     public Queue queueC() {
    >         return new Queue("fanout.C");
    >     }
    > 
    >     @Bean
    >     FanoutExchange fanoutExchange() {
    >         return new FanoutExchange("fanoutExchange");
    >     }
    > 
    >     @Bean
    >     Binding bindingExchangeA() {
    >         return BindingBuilder.bind(queueA()).to(fanoutExchange());
    >     }
    > 
    >     @Bean
    >     Binding bindingExchangeB() {
    >         return BindingBuilder.bind(queueB()).to(fanoutExchange());
    >     }
    > 
    >     @Bean
    >     Binding bindingExchangeC() {
    >         return BindingBuilder.bind(queueC()).to(fanoutExchange());
    >     }
    > 
    > }
    > ```

  * > ``` java
    > // receiver
    > @Component
    > @RabbitListener(queues = "fanout.A")
    > public class FanoutReceiverA {
    > 
    >     @RabbitHandler
    >     public void process(Map testMessage) {
    >         System.out.println("FanoutReceiverA消费者收到消息  : " +testMessage.toString());
    >     }
    > }
    > 
    > @Component
    > @RabbitListener(queues = "fanout.B")
    > public class FanoutReceiverB {
    > 
    >     @RabbitHandler
    >     public void process(Map testMessage) {
    >         System.out.println("FanoutReceiverB消费者收到消息  : " +testMessage.toString());
    >     }
    > }
    > 
    > @Component
    > @RabbitListener(queues = "fanout.C")
    > public class FanoutReceiverC {
    > 
    >     @RabbitHandler
    >     public void process(Map testMessage) {
    >         System.out.println("FanoutReceiverC消费者收到消息  : " +testMessage.toString());
    >     }
    > }
    > 
    > // 扇形交换机没有路由键的概念，所有绑定到该交换机上的队列都会接收到发送到该交换机上的消息
    > ```
    >
    > 

### 主题交换机（Topic Exchange）

* 跟直连交换机流程差不多，它的**特点**就是在它的路由键和绑定键之间是有规则的。
  简单地介绍下规则：

  > *(星号)： 用来表示一个单词 (必须出现的)
  >
  > #(井号) ：用来表示任意数量（零个或多个）单词

* ==**生产者**==

  * > ``` java
    > // config
    > @Configuration
    > public class TopicRabbitConfig {
    >     //绑定键
    >     public final static String part = "topic.part";
    >     public final static String all = "topic.all";
    > 
    >     @Bean
    >     public Queue firstQueue() {
    >         return new Queue(TopicRabbitConfig.part);
    >     }
    > 
    >     @Bean
    >     public Queue secondQueue() {
    >         return new Queue(TopicRabbitConfig.all);
    >     }
    > 
    >     @Bean
    >     TopicExchange exchange() {
    >         return new TopicExchange("topicExchange");
    >     }
    > 
    > 
    >     //将firstQueue和topicExchange绑定,而且绑定的键值为topic.man
    >     //这样只要是消息携带的路由键是topic.man,才会分发到该队列
    >     @Bean
    >     Binding bindingExchangeMessage() {
    >         return BindingBuilder.bind(firstQueue()).to(exchange()).with(part);
    >     }
    > 
    >     //将secondQueue和topicExchange绑定,而且绑定的键值为用上通配路由键规则topic.#
    >     // 这样只要是消息携带的路由键是以topic.开头,都会分发到该队列
    >     @Bean
    >     Binding bindingExchangeMessage2() {
    >         return BindingBuilder.bind(secondQueue()).to(exchange()).with("topic.#");
    >     }
    > 
    > }
    > ```

  * > ``` java
    > // sender
    > @Component
    > public class TopicMqService {
    > 
    >     @Autowired
    >     RabbitTemplate rabbitTemplate;
    > 
    >     public void sendTopicMessage1(String message) {
    >         String messageId = String.valueOf(UUID.randomUUID());
    >         String messageData = message;
    >         String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    >         Map<String, Object> manMap = new HashMap<>();
    >         manMap.put("messageId", messageId);
    >         manMap.put("messageData", messageData);
    >         manMap.put("createTime", createTime);
    >         rabbitTemplate.convertAndSend("topicExchange", "topic.part", manMap);
    >     }
    > 
    >     public void sendTopicMessage2(String message) {
    >         String messageId = String.valueOf(UUID.randomUUID());
    >         String messageData = message;
    >         String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    >         Map<String, Object> womanMap = new HashMap<>();
    >         womanMap.put("messageId", messageId);
    >         womanMap.put("messageData", messageData);
    >         womanMap.put("createTime", createTime);
    >         rabbitTemplate.convertAndSend("topicExchange", "topic.all", womanMap);
    >     }
    > }
    > ```

* ==**消费者**==

  * > ``` java
    > // config，消费者单纯的使用，其实可以不用添加这个配置，直接建后面的监听就好，使用注解来让监听器监听对应的队列即可。配置上了的话，其实消费者也是生成者的身份，也能推送该消息
    > @Configuration
    > public class TopicRabbitConfig {
    >     //绑定键
    >     public final static String part = "topic.part";
    >     public final static String all = "topic.all";
    > 
    >     @Bean
    >     public Queue firstQueue() {
    >         return new Queue(TopicRabbitConfig.part);
    >     }
    > 
    >     @Bean
    >     public Queue secondQueue() {
    >         return new Queue(TopicRabbitConfig.all);
    >     }
    > 
    >     @Bean
    >     TopicExchange exchange() {
    >         return new TopicExchange("topicExchange");
    >     }
    > 
    >     //将firstQueue和topicExchange绑定,而且绑定的键值为topic.man
    >     //这样只要是消息携带的路由键是topic.man,才会分发到该队列
    >     @Bean
    >     Binding bindingExchangeMessage() {
    >         return BindingBuilder.bind(firstQueue()).to(exchange()).with(part);
    >     }
    > 
    >     //将secondQueue和topicExchange绑定,而且绑定的键值为用上通配路由键规则topic.#
    >     // 这样只要是消息携带的路由键是以topic.开头,都会分发到该队列
    >     @Bean
    >     Binding bindingExchangeMessage2() {
    >         return BindingBuilder.bind(secondQueue()).to(exchange()).with("topic.#");
    >     }
    > }
    > ```

  * > ``` java
    > // resolver
    > @Component
    > @RabbitListener(queues = "topic.part")
    > public class TopicPartReceiver {
    > 
    >     @RabbitHandler
    >     public void process(Map message) {
    >         System.out.println("part topic message: " + message);
    >     }
    > }
    > 
    > @Component
    > @RabbitListener(queues = "topic.all")
    > public class TopicAllReceiver {
    >     @RabbitHandler
    >     public void process(Map message) {
    >         System.out.println("all topic message: " + message);
    >     }
    > }
    > ```

## 消息回调（生产者）

* 配置

  * ``` yaml
    server:
      port: 8021
    spring:
      #给项目来个名字
      application:
        name: rabbitmq-provider
      #配置rabbitMq 服务器
      rabbitmq:
        host: 193.112.82.144
        port: 5672
        username: guest
        password: guest
        #虚拟host 可以不设置,使用server默认host
    #    virtual-host: host
        #确认消息已发送到交换机(Exchange)
        publisher-confirm-type: correlated
        #确认消息已发送到队列(Queue)
        publisher-returns: true
    
    ```

  * ``` java
    @Configuration
    public class RabbitConfig {
     
        @Bean
        public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
            RabbitTemplate rabbitTemplate = new RabbitTemplate();
            rabbitTemplate.setConnectionFactory(connectionFactory);
            //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
            rabbitTemplate.setMandatory(true);
     
            rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
                @Override
                public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                    System.out.println("ConfirmCallback:     "+"相关数据："+correlationData);
                    System.out.println("ConfirmCallback:     "+"确认情况："+ack);
                    System.out.println("ConfirmCallback:     "+"原因："+cause);
                }
            });
     
            rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
                @Override
                public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                    System.out.println("ReturnCallback:     "+"消息："+message);
                    System.out.println("ReturnCallback:     "+"回应码："+replyCode);
                    System.out.println("ReturnCallback:     "+"回应信息："+replyText);
                    System.out.println("ReturnCallback:     "+"交换机："+exchange);
                    System.out.println("ReturnCallback:     "+"路由键："+routingKey);
                }
            });
     
            return rabbitTemplate;
        }
     
    }
    ```

## 消息接收确认（消费者）

### 消息接收确认三种模式

* **自动确认（默认）**

  * AcknowledgeMode.NONE

  * > RabbitMQ成功将消息发出（即将消息成功写入TCP Socket）中立即认为本次投递已经被正确处理，不管消费者端是否成功处理本次投递。
    > 所以这种情况如果消费端消费逻辑抛出异常，也就是消费端没有处理成功这条消息，那么就相当于丢失了消息。
    > 一般这种情况我们都是使用try catch捕捉异常后，打印日志用于追踪数据，这样找出对应数据再做后续处理。

* **根据情况确认**

  * 不做介绍

* ==**手动确认**==

  * **`配置消息确认机制时，多数选择的模式`**
  * 消费者收到消息后，手动调用basic.ack/basic.nack/basic.reject后，RabbitMQ收到这些消息后，才认为本次投递成功。
  * `basic.ack`用于肯定确认 
  * `basic.nack`用于否定确认（注意：这是AMQP 0-9-1的RabbitMQ扩展） 
    * `channel.basicNack(deliveryTag, false, true)`
      * 第一个参数依然是当前消息到的数据的唯一id; 
      * 第二个参数是指是否针对多条消息；如果是true，也就是说一次性针对当前通道的消息的tagID小于当前这条消息的，都拒绝确认。
      * 第三个参数是指是否重新入列，也就是指不确认的消息是否重新丢回到队列里面去。
    * 同样使用不确认后重新入列这个确认模式要谨慎，因为这里也可能因为考虑不周出现消息一直被重新丢回去的情况，导致积压。
  * `basic.reject`用于否定确认，但与basic.nack相比有一个限制:一次只能拒绝单条消息 
    * `channel.basicReject(deliveryTag, true)`;  拒绝消费当前消息，如果第二参数传入true，就是将数据重新丢回队列里，那么下次还会消费这消息。设置false，就是告诉服务器，我已经知道这条消息数据了，因为一些原因拒绝它，而且服务器也把这个消息丢掉就行。 下次不想再消费这条消息了。
    * 使用拒绝后重新入列这个确认模式要谨慎，因为一般都是出现异常的时候，catch异常再拒绝入列，选择是否重入列。但是如果使用不当会导致一些每次都被你重入列的消息一直消费-入列-消费-入列这样循环，会导致消息积压。