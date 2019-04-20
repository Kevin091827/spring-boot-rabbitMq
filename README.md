#### 1. AMQP协议
**1.简介**
AMQP（Advanced Message Queuing Protocol，高级消息队列协议）是一个进程间传递异步消息的l网络协议

**2.AMQP模型**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190411002317206.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkyMjI4OQ==,size_16,color_FFFFFF,t_70)
**3.工作流程**
消息发布者发布消息给交换机，交换机根据传递过来的路由键按照路由规则将接收到的消息分发给和交换机绑定的相应的队列，最后交给消息消费者


#### 2. RabbitMQ
**1.简介**
RabbitMQ是实现AMQP（高级消息队列协议）的消息中间件的一种

**2.工作流程**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190411001941123.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkyMjI4OQ==,size_16,color_FFFFFF,t_70)
**相关概念**
* 虚拟主机：
	一个虚拟主机持有医嘱交换机，队列和绑定，rabbitMQ中一般建议有多个虚拟主机，为什么需要多个虚拟主机呢？很简单，rabbitMQ中，用户只能在虚拟主机的粒度进行权限控制，因此，如果需要禁止A组访问B组的交换机/队列/绑定，必须为A组合B租分别创建一个虚拟主机，默认虚拟主机“/“。
* 交换机：
	作用：转发消息，不会存储消息，如果没有队列绑定到交换机，交换机会直接丢弃生产者发来时的消息
* 路由键：
	消息发送到交换机的时候，交换机会根据路由键转发到对应的队列中
* 绑定：
	交换机和队列相绑定，一个交换机可绑定多个队列


**生产消费模型**
direct----匹配规则为：如果路由键匹配，消息就被投送到相关的队列

![在这里插入图片描述](https://img-blog.csdnimg.cn/2019041100352220.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkyMjI4OQ==,size_16,color_FFFFFF,t_70)

fanout----交换器中没有路由键的概念，他会把消息发送到所有绑定在此交换器上面的队列中。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190411003531402.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkyMjI4OQ==,size_16,color_FFFFFF,t_70)

topic----交换器你采用模糊匹配路由键的原则进行转发消息到队列中

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190411003541136.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkyMjI4OQ==,size_16,color_FFFFFF,t_70)

**3.使用场景**
RabbitMQ 即一个消息队列，主要是用来实现应用程序的异步和解耦，同时也能起到消息缓冲，消息分发的作用。

#### 3.springboot 整合RabbitMQ
**1.引入依赖**

springboot集成RabbitMQ非常简单，如果只是简单的使用配置非常少，springboot提供了spring-boot-starter-amqp项目对消息各种支持。
```xml
	    <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
```
**2.配置端口，账户信息**
```application
############  RabbitMQ  #################
#spring.rabbitmq.host=127.0.0.1
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
#开启发布确认机制
spring.rabbitmq.publisher-confirms=true
#是否创建AmqpAdmin bean. 默认为: true
spring.rabbitmq.dynamic=true
spring.rabbitmq.cache.connection.mode=channel
#指定最小的消费者数量.
#spring.rabbitmq.listener.concurrency
#指定最大的消费者数量
#spring.rabbitmq.listener.max-concurrency
```
**3.设置队列，交换机**
```java
    /**
     * 设置队列
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(DirectKeyInterface.DIRECT_QUEUE_NAME);
    }
    
    /**	
     * 设置交换机
     * @return
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(DirectKeyInterface.DIRECT_EXCHANGE_NAME);
    }
```
**4.绑定队列，交换机和路由键**

如果rabbitMQ管理平台中还没有，则会自动新增

```java
    /**
     * 在fanout模式下绑定交换机，队列，路由键
     * @param directExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding binding_direct(DirectExchange directExchange,Queue queue){
        return BindingBuilder.bind(queue).to(directExchange).with(DirectKeyInterface.DIRECT_KEY);
    }
```
**5.生产者，消费者**

生产者 --- 消息发送
```java
    /**
     * 消息发送(点对点）
     * @param message
     */
    public void send(String message){
       amqpTemplate.convertAndSend(DirectKeyInterface.DIRECT_EXCHANGE_NAME,DirectKeyInterface.DIRECT_KEY,message);
       log.info("消息发送成功：{}",message);
    }
```

消费者 --- 接收消息
```java
    /**
     * 接收消息（自动监听）
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = DirectKeyInterface.DIRECT_QUEUE_NAME)//监听指定队列
    public void recevice(String message){
        log.info("接收到消息：{}",message);
    }
```
**6.测试**
```java
    @Autowired
    private Producer producer;

    /**
     * 发送消息
     * @return
     */
    @GetMapping("/helloRabbitMQ")
    public String sendMsg(){
        producer.send("hello world!");
        //producer.sendOfFanout("hello!");
        //producer.sendOfTopic("hello topic!");
        return "success";
    }
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190413013404781.png)

--------------------------------------------------更新----------------------------------------------

#### 4.rabbitMQ消息确认机制

* **为什么要进行消息确认？**
    
    发送者在发送消息后，无法确认消息是否真正发送到相应交换机中
    ，如果消费者将消息发送到正确的交换机上，但是路由不到正确的队列，而消费者也无法保证能正确从队列中获取到相应的消息
    ，rabbitMQ默认是没有反馈的，所以我们需要确认消息是否真正到达
    
* **消息确认机制实现方式**

    **1. AMQP事务机制**
    
    --- txSelect:  用户当前channel设置成事务模式
    
    --- txCommit:  提交事务
    
    --- txRollback:  回滚事务
    
    此种方式很耗时，降低了消息的吞吐量
    
    **2. Confirm模式**
    
    生产者将channel设置成Confirm模式，该channel上的消息都会被
    指派一个id来表示消息，一旦消息被发送到相应的队列之后，broker
    就会发生一个确认消息（包含消息id）给生产者，如果消息和队列是可持久化的
    ，那么确认消息是在消息写入磁盘后发送，
    
    最大好处：异步处理
    
    confirmSelect 模式：
    
    ---普通
    
        每发送一条消息就执行一次waitForConfirm()
   
    ---批量
    
        发送多条消息后在执行waitForConfirm()
    
    ---异步 
    
        提供一个回调方法来实现消息确认confirmListener()
    
        

**1.消息发送确认 与 消息接收确认（ACK）**
* **消息发送确认：**
   
   当消息可能因为路由键不匹配或者发送不到指定交换机而导致无法发送到相应队列时
   确认消息发送失败，相反，确认消息发送成功

* **消息接收确认：**
    
    1.消息通过 ACK 确认是否被正确接收，每个 Message 都要被确认（acknowledged），可以手动去 ACK 或自动 ACK
    
    2.自动确认会在消息发送给消费者后立即确认，但存在丢失消息的可能，如果消费端消费逻辑抛出异常，也就是消费端没有处理成功这条消息，那么就相当于丢失了消息
    
    3.如果消息已经被处理，但后续代码抛出异常，使用 Spring 进行管理的话消费端业务逻辑会进行回滚，这也同样造成了实际意义的消息丢失
    
    4.如果手动确认则当消费者调用 ack、nack、reject 几种方法进行确认，手动确认可以在业务失败后进行一些操作，如果消息未被 ACK 则会发送到下一个消费者
    
    5.如果某个服务忘记 ACK 了，则 RabbitMQ 不会再发送数据给它，因为 RabbitMQ 认为该服务的处理能力有限
    
    6.ACK 机制还可以起到限流作用，比如在接收到某条消息时休眠几秒钟
    
    7.消息确认模式有：
    
      AcknowledgeMode.NONE：自动确认
      AcknowledgeMode.AUTO：根据情况确认
      AcknowledgeMode.MANUAL：手动确认
      
**2.两个接口**

**ConfirmCallback**

实现ConfirmCallback接口实现消息发送到交换机的回调

```java
@Slf4j
public class RabbitConfirmCallback implements RabbitTemplate.ConfirmCallback{
    /**
     * 发送到交换机上失败回调
     * 消息发送回调(判断是否发送到相应的交换机上)
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("消息发送到exchange成功");
        } else {
            log.info("消息发送到exchange失败");
        }
    }
}
```

**ReturnCallback**

实现ReturnCallback接口实现消息失败回调，当消息路由不到指定队列时回调方法

```java

@Slf4j
public class RabbitReturnCallback implements RabbitTemplate.ReturnCallback {
    /**
     * 发送到队列失败后回调
     * 消息可以发送到相应交换机，但是没有相应路由键和队列绑定
     * @param message
     * @param i
     * @param s
     * @param s1
     * @param s2
     */
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        log.info("消息发送失败");
    }
}
```
最后只需要重新配置rabbitTemplate即可
```java
   /**
     * 定制rabbitMQ模板
     *
     * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收消息成功回调
     * ReturnCallback接口用于实现消息发送到RabbitMQ交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中回调
     * @return
     */
    @PostConstruct
    public void initRabbitTemplate(){

        rabbitTemplate.setConfirmCallback(new RabbitConfirmCallback());
        rabbitTemplate.setReturnCallback(new RabbitReturnCallback());
    }
```
补充：

@PostConstruct和@PreConstruct。这两个注解被用来修饰一个非静态的void()方法.而且这个方法不能有抛出异常声明。

```java
    @PostConstruct                    //方式1
    public void someMethod(){
        ...
    }
    
    @PreConstruct 
    public void someMethod(){        //方式2
        ...  
    }
```
@PostConstruct

 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器调用一次，类似于Serclet的inti()方法。
 被@PostConstruct修饰的方法会在构造函数之后，init()方法之前运行。
 
 @PreConstruct
 
 被@PreDestroy修饰的方法会在服务器卸载Servlet的时候运行，并且只会被服务器调用一次，类似于Servlet的destroy()方法。被@PreDestroy修饰的方法会在destroy()方法之后运行，在Servlet被彻底卸载之前。
 
 #### 5.rabbitMQ消息持久化机制
 
为了保证消息的可靠性，需要对消息进行持久化。解决rabbitMQ服务器异常导致数据丢失

为了保证RabbitMQ在重启、奔溃等异常情况下数据没有丢失，除了对消息本身持久化为，还需要将消息传输经过的队列(queue)，交互机进行持久化(exchange)，持久化以上元素后，消息才算真正RabbitMQ重启不会丢失。


>详细参数：

>durable :是否持久化，如果true，则此种队列叫持久化队列（Durable queues）。此队列会被存储在磁盘上，当消息代理（broker）重启的时候，它依旧存在。没有被持久化的队列称作暂存队列（Transient queues）。 
 
>execulusive :表示此对应只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable 

>autoDelete: 
 当没有生成者/消费者使用此队列时，此队列会被自动删除。 
 (即当最后一个消费者退订后即被删除)
 
 eg:
 ```java
  /**
     * 设置持久化topic模式队列
     * durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
     * exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
     * autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
     * @return
     */
    @Bean
    public Queue durableTopicQueue(){
        return new Queue(TopicKeyInterface.TOPIC_DURABLE_QUEUE_NAME,true,true,false);
    }
    
     /**
         * 设置持久化交换机
         * durable:
         * autoDelete:
         * @return
         */
        @Bean
        public TopicExchange durableTopicExchange(){
            return new TopicExchange(TopicKeyInterface.TOPIC_DURABLE_QUEUE_NAME,true,false);
        }
```

 #### 6.rabbitMQ消费者消息手动ACK确认
 
 ##### 1.ACK机制：消息确认机制
 **1.作用：** 
 
 * 确认消息是否被消费者消费，消息通过ACK机制确认是否被正确接收，每个消息都要被确认。
 
 * 默认情况下，一个消息被消费者正确消费就会从队列中移除
 
 **2.ACK确认模式**
 
 * **AcknowledgeMode.NONE ：不确认**
 
 	**1**. 默认所有消息消费成功，会不断的向消费者推送消息
 		
 	**2**. 因为rabbitMq认为所有消息都被消费成功，所以队列中不在存有消息，消息存在丢失的危险
 
 * **AcknowledgeMode.AUTO：自动确认**
 		
 	**1**. 由spring-rabbit依据消息处理逻辑是否抛出异常自动发送ack（无异常）或nack（异常）到server端。				      存在丢失消息的可能，如果消费端消费逻辑抛出异常，也就是消费端没有处理成功这条消息，那么就相当于丢失了消息
 如果消息已经被处理，但后续代码抛出异常，使用 Spring 进行管理的话消费端业务逻辑会进行回滚，这也同样造成了实际意义的消息丢失
 		
 	**2**. 使用自动确认模式时，需要考虑的另一件事是消费者过载
 
 * **AcknowledgeMode.MANUAL：手动确认**
 		
 	**1**. 手动确认则当消费者调用 ack、nack、reject 几种方法进行确认，手动确认可以在业务失败后进行一些操作，如果消息未被 ACK 则会发送到下一个消费者
 		
 	**2**. 手动确认模式可以使用 prefetch，限制通道上未完成的（“正在进行中的”）发送的数量
 
 **3. 忘记ACK确认**
 
 忘记通过basicAck返回确认信息是常见的错误。这个错误非常严重，将导致消费者客户端退出或者关闭后，消息会被退回RabbitMQ服务器，这会使RabbitMQ服务器内存爆满，而且RabbitMQ也不会主动删除这些被退回的消息。只要程序还在运行，没确认的消息就一直是 Unacked 状态，无法被 RabbitMQ 重新投递。更厉害的是，RabbitMQ 消息消费并没有超时机制，也就是说，程序不重启，消息就永远是 Unacked 状态。处理运维事件时不要忘了这些 Unacked 状态的消息。当程序关闭时（实际只要 消费者 关闭就行），消息会恢复为 Ready 状态。
 
 ##### 2.局部消息确认
 
 **开启手动ack确认**
 
 ```application
 #消息确认机制 --- 是否开启手ack动确认模式
 spring.rabbitmq.listener.direct.acknowledge-mode=manual
 #消息确认机制 --- 是否开启手ack动确认模式
 spring.rabbitmq.listener.simple.acknowledge-mode=manual
 ```
 
 **配置消费者**
 
 ```java
 @RabbitHandler
     @RabbitListener(queues = DirectKeyInterface.DIRECT_QUEUE_NAME)
     public void receiveObjectDel(Channel channel, String json, Message message,@Headers Map<String,Object> map){
 
         log.info("接收到的id："+json);
         int id = Integer.parseInt(json);
         registerDao.deleteUser(id);
 
         //<P>代码为在消费者中开启消息接收确认的手动ack</p>
         //<H>配置完成</H>
         //<P>可以开启全局配置</p>
         if (map.get("error")!= null){
             log.info("错误的消息");
             try {
                 //否认消息,拒接该消息重回队列
                 channel.basicNack((Long)map.get(AmqpHeaders.DELIVERY_TAG),false,false);
                 return;
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
         //手动ACK
         //默认情况下如果一个消息被消费者所正确接收则会被从队列中移除
         //如果一个队列没被任何消费者订阅，那么这个队列中的消息会被 Cache（缓存），
         //当有消费者订阅时则会立即发送，当消息被消费者正确接收时，就会被从队列中移除
         try {
             //手动ack应答
             //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了
             // 否则消息服务器以为这条消息没处理掉 后续还会在发，true确认所有消费者获得的消息
             channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
             log.info("消息消费成功：id：{}",message.getMessageProperties().getDeliveryTag());
         } catch (IOException e) {
             e.printStackTrace();
             //丢弃这条消息
             try {
                 //最后一个参数是：是否重回队列
                 channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
                 //拒绝消息
                 //channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
                 //消息被丢失
                 //channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                 //消息被重新发送
                 //channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
                 //多条消息被重新发送
                 //channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
             } catch (IOException e1) {
                 e1.printStackTrace();
             }
             log.info("消息消费失败：id：{}",message.getMessageProperties().getDeliveryTag());
         }
     }
 ```
 ##### 3.全局消息确认
 
发现一个问题，如果全局配置消费者消息ACk确认，会出现一个问题，就是有部分消息在没有被监听到的情况下可能会因为被消费掉而从队列中
 移除，从而没有执行到业务代码，所以暂时还是局部在消费者配置
 ```java
     /**
      * 消费者全局消息手动ACK确认(还没配置完成)
      * @return
      */
     @Bean
     public SimpleMessageListenerContainer messageListenerContainer(){
 
         SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
         container.setConnectionFactory(connectionFactory());
         //监听的队列（是一个String类型的可变参数,将监听的队列配置上来，可减少在消费者中代码量）
         container.setQueueNames(DirectKeyInterface.DIRECT_QUEUE_NAME);
         //手动确认
         container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
         container.setMessageListener(channelAwareMessageListener());
     
         //消息处理
         container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
             log.info("====接收到消息=====");
             log.info(new String(message.getBody()));
             //它会根据方法的执行情况来决定是否确认还是拒绝（是否重新入queue）
                 //1.抛出NullPointerException异常则重新入队列
                     //throw new NullPointerException("消息消费失败");
                 //2.当抛出的异常是AmqpRejectAndDontRequeueException异常的时候，则消息会被拒绝，且requeue=false
                     //throw new AmqpRejectAndDontRequeueException("消息消费失败");
                 //3.当抛出ImmediateAcknowledgeAmqpException异常，则消费者会被确认
                     //throw new ImmediateAcknowledgeAmqpException("消息消费失败");
             //消息手动弄ACK确认
             if(message.getMessageProperties().getHeaders().get("error") == null){
 
                 try {
                     //手动ack应答
                     //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了
                     // 否则消息服务器以为这条消息没处理掉 后续还会在发，true确认所有消费者获得的消息
                     channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                     log.info("消息消费成功：id：{}",message.getMessageProperties().getDeliveryTag());
                 } catch (IOException e) {
                     e.printStackTrace();
                     //丢弃这条消息
                     try {
                         //最后一个参数是：是否重回队列
                         channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,true);
                         //拒绝消息
                         //channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
                         //消息被丢失
                         //channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                         //消息被重新发送
                         //channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
                         //多条消息被重新发送
                         //channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
                     } catch (IOException e1) {
                         e1.printStackTrace();
                     }
                     log.info("消息消费失败：id：{}",message.getMessageProperties().getDeliveryTag());
                 }
             }else {
                 //处理错误消息，拒觉错误消息重新入队
                 channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
                 channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
                 log.info("消息拒绝");
             }
         });
 
         return container;
     }
 ```
 我已经抽象出一个工具类来负责消费者手动ACK确认


 #### 7.RabbitMQ延时消费
 
 ##### 应用场景
 - **延迟消费**:
 
 比如： 用户生成订单之后，需要过一段时间校验订单的支付状态，如果订单仍未支付则需要及时地关闭订单。
 
 用户注册成功之后，需要过一段时间比如一周后校验用户的使用情况，如果发现用户活跃度较低，则发送邮件或者短信来提醒用户使用。
 
 - **延迟重试**：
 
 比如消费者从队列里消费消息时失败了，但是想要延迟一段时间后自动重试。
 
 如果不使用延迟队列，那么我们只能通过一个轮询扫描程序去完成。这种方案既不优雅，也不方便做成统一的服务便于开发人员使用。但是使用延迟队列的话，我们就可以轻而易举地完成。
 
 ### 二，springboot实现rabbitMQ延时队列
 #### 实现思路
 ###### RabbitMQ两大特性
 
 **RabbitMQ消息的死亡方式：**
 
 - 消息被拒绝，通过调用basic.reject或者basic.nack并且设置的requeue参数为false。
 - 消息设置了存活时间
 - 消息进入了一条已经达到最大长度的队列
 
 **Time-To-Live Extensions**
 
 rabbitmq允许我们为消息或者队列设置过期时间，也就是TTL，TTL的意思是一条消息在队列中最大的存活时间，单位是毫秒，当某条消息被设置了TTL或者当某条消息进入了设置了TTL的队列时，这条消息会在经过TTL秒后“死亡”，成为Dead Letter。如果既配置了消息的TTL，又配置了队列的TTL，那么较小的那个值会被取用。
 
 
 **Dead Letter Exchange**
 
 如果队列设置了Dead Letter Exchange（DLX），那么这些Dead Letter就会被重新publish到Dead Letter Exchange，通过Dead Letter Exchange路由到其他队列
 
 ##### 实现流程
 * **延迟消费**
 
 生产者产生的消息首先会进入缓冲队列（图中红色队列）。通过RabbitMQ提供的TTL扩展，这些消息会被设置过期时间，也就是延迟消费的时间。等消息过期之后，这些消息会通过配置好的DLX转发到实际消费队列（图中蓝色队列），以此达到延迟消费的效果。
 
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190419213745548.png)
 
 * **延迟重试**
 
 消费者发现该消息处理出现了异常，比如是因为网络波动引起的异常。那么如果不等待一段时间，直接就重试的话，很可能会导致在这期间内一直无法成功，造成一定的资源浪费。那么我们可以将其先放在缓冲队列中（图中红色队列），等消息经过一段的延迟时间后再次进入实际消费队列中（图中蓝色队列），此时由于已经过了“较长”的时间了，异常的一些波动通常已经恢复，这些消息可以被正常地消费。
 
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190419213834279.png)
 
 ##### springboot实现rabbitMQ延迟消费
 
 延迟消费相关配置
 ```java
 @Configuration
 public class DelayConfig {
 
     /**
      * 延时交换机 --- 交换机用于重新分配队列（接收死信队列中的过期消息，将其转发到需要延迟消息的模块队列）
      * @return
      */
     @Bean
     public DirectExchange exchange() {
         return new DirectExchange(DelayKeyInterface.DELAY_EXCHANGE);
     }
 
     /**
      * 实际消费队列
      * 用于延时消费的队列
      */
     @Bean
     public Queue repeatTradeQueue() {
         Queue queue = new Queue(DelayKeyInterface.DELAYMSG_RECEIVE_QUEUE_NAME,true,false,false);
         return queue;
     }
 
     /**
      * 绑定交换机并指定routing key（死信队列绑定延迟交换机和实际消费队列绑定延迟交换机的路由键一致）
      * @return
      */
     @Bean
     public Binding  repeatTradeBinding() {
         return BindingBuilder.bind(repeatTradeQueue()).to(exchange()).with(DelayKeyInterface.DELAY_KEY);
     }
 
     //死信队列
     @Bean
     public Queue deadLetterQueue() {
         Map<String,Object> args = new HashMap<>();
         args.put("x-message-ttl", DelayKeyInterface.EXPERI_TIME);
         args.put("x-dead-letter-exchange", DelayKeyInterface.DELAY_EXCHANGE);
         args.put("x-dead-letter-routing-key", DelayKeyInterface.DELAY_KEY);
         return new Queue(DelayKeyInterface.DELAY_QUEUE_NAME, true, false, false, args);
     }
 
 }
 
 ```
 
 发送消息
 ```java
     /**
      * 发送延迟消息
      * @return
      */
     @GetMapping("/send")
     public String sendDelayMsg(){
         rabbitTemplate.convertAndSend(DelayKeyInterface.DELAY_QUEUE_NAME,"hello");
         log.info("发送时间："+ LocalDateTime.now());
         return "success";
     }
 ```
 接收延迟消息：
 ```java
     /**
      * 接收延迟消息
      * @param channel
      * @param json
      * @param message
      * @param map
      */
     @RabbitHandler
     @RabbitListener(queues = DelayKeyInterface.DELAYMSG_RECEIVE_QUEUE_NAME)
     public void receiveDelayMsg(Channel channel, String json, Message message,@Headers Map<String,Object> map){
 
         log.info("接收到的消息"+json);
         log.info("接收时间："+ LocalDateTime.now());
         //<P>代码为在消费者中开启消息接收确认的手动ack</p>
         //<H>配置完成</H>
         //<P>可以开启全局配置</p>
         AckUtils.ack(channel,message,map);
     }
 ```
 结果：
 
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190420011059332.png)
 
 





