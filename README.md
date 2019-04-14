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
当我们程序向rabbitMQ中间件发送消息时，如果程序没什么异常的话，一般都会成功发送消息
但是，我们并不知道，消息是否成功发送到相应交换机的相应队列中，此时，我们需要用到消息确认机制，
这也是rabbitMQ的一个功能点

**1.消息发送确认 与 消息接收确认（ACK）**
* 消息发送确认：
   
   当消息可能因为路由键不匹配或者发送不到指定交换机而导致无法发送到相应队列时
   确认消息发送失败，相反，确认消息发送成功

* 消息接收确认：
    
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
