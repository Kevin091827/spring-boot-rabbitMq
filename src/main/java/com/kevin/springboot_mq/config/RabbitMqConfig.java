package com.kevin.springboot_mq.config;

import com.kevin.springboot_mq.config.delay.DelayRetryKeyInterface;
import com.kevin.springboot_mq.config.direct.DirectKeyInterface;
import com.kevin.springboot_mq.message.Consumer;
import com.kevin.springboot_mq.service.RegisterService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description:    rabbitMq一些通用配置
 * @Author:         Kevin
 * @CreateDate:     2019/4/13 23:03
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/13 23:03
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
@Configuration
@Slf4j
public class RabbitMqConfig {

    @Autowired
    private Environment env;

    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    /**
     * 定制rabbitMQ模板
     * <p>
     * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收消息成功回调
     * ReturnCallback接口用于实现消息发送到RabbitMQ交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中回调
     *</p>
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {

        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        //配置连接信息
        rabbitTemplate.setConnectionFactory(connectionFactory());
        //开启消息确认机制
        rabbitTemplate.setMandatory(true);
        //配置消息确认机制
        rabbitTemplate.setConfirmCallback(new RabbitConfirmCallback());
        //配置消息确认机制
        rabbitTemplate.setReturnCallback(new RabbitReturnCallback());
        return rabbitTemplate;
    }

    /**
     * 连接工厂（配置连接信息）
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(env.getProperty("spring.rabbitmq.host"),
                                                                                  env.getProperty("spring.rabbitmq.port",int.class));
        //用户名
        connectionFactory.setUsername(env.getProperty("spring.rabbitmq.username"));
        //密码
        connectionFactory.setPassword(env.getProperty("spring.rabbitmq.password"));
        //虚拟主机
        connectionFactory.setVirtualHost(env.getProperty("spring.rabbitmq.virtual-host"));
        //消息确认机制 --- 是否回调(默认false）
        connectionFactory.setPublisherConfirms(env.getProperty("spring.rabbitmq.publisher-confirms",Boolean.class));
        //消息确认机制 --- 是否返回回调(默认false）
        connectionFactory.setPublisherReturns(env.getProperty("spring.rabbitmq.publisher-returns",Boolean.class));
        return connectionFactory;
    }

    /**
     * RabbitMQ监听器容器 --- 配置@RabbitListener
     * 单个消费者
     * @return
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        //消息类型转换
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //最小消费者数量
        factory.setConcurrentConsumers(1);
        //最大消费者数量
        factory.setMaxConcurrentConsumers(1);
        //每个消费者每次监听时可拉取处理的消息数量。
        factory.setPrefetchCount(1);
        //事务
        //factory.setTxSize(1);
        //消息ack确认（手动确认）
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    /**
     * RabbitMQ监听器容器 --- 配置@RabbitListener
     * 多个消费者
     * @return
     */
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //配置连接
        factoryConfigurer.configure(factory,connectionFactory());
        //消息类型转换
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //消息ack确认（手动确认）
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //消费者最少数量
        factory.setConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.concurrency",int.class));
        //消费者最大数量
        factory.setMaxConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.max-concurrency",int.class));
        //每个消费者每次监听时可拉取处理的消息数量。
        factory.setPrefetchCount(env.getProperty("spring.rabbitmq.listener.prefetch",int.class));
        return factory;
    }

    @Bean
    SimpleMessageListenerContainer processContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(DelayRetryKeyInterface.RETRY_CONSUME_QUEUE); // 监听
        return container;
    }



//    /**
//     * 消费者全局消息手动ACK确认(还没配置完成)
//     * @return
//     */
//   // @Bean
//    public SimpleMessageListenerContainer messageListenerContainer(){
//
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setExposeListenerChannel(true);
//        container.setConnectionFactory(connectionFactory());
//        //监听的队列（是一个String类型的可变参数,将监听的队列配置上来，可减少在消费者中代码量）
//        container.setQueueNames(DirectKeyInterface.DIRECT_QUEUE_NAME);
//        //手动确认
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        container.setMessageListener(channelAwareMessageListener());
//
//        /*
//        //消息处理
//        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
//            log.info("====****接收到消息****=====");
//            log.info(new String(message.getBody()));
//            //它会根据方法的执行情况来决定是否确认还是拒绝（是否重新入queue）
//                //1.抛出NullPointerException异常则重新入队列
//                    //throw new NullPointerException("消息消费失败");
//                //2.当抛出的异常是AmqpRejectAndDontRequeueException异常的时候，则消息会被拒绝，且requeue=false
//                    //throw new AmqpRejectAndDontRequeueException("消息消费失败");
//                //3.当抛出ImmediateAcknowledgeAmqpException异常，则消费者会被确认
//                    //throw new ImmediateAcknowledgeAmqpException("消息消费失败");
//            //消息手动弄ACK确认
//            if(message.getMessageProperties().getHeaders().get("error") == null){
//
//                try {
//
//                    //手动ack应答
//                    //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了
//                    // 否则消息服务器以为这条消息没处理掉 后续还会在发，true确认所有消费者获得的消息
//                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
//                    log.info("***消息消费成功：id：{}****",message.getMessageProperties().getDeliveryTag());
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    //丢弃这条消息
//                    try {
//                        //最后一个参数是：是否重回队列
//                        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
//                        //拒绝消息
//                        //channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
//                        //消息被丢失
//                        //channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
//                        //消息被重新发送
//                        //channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
//                        //多条消息被重新发送
//                        //channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
//                    log.info("****消息消费失败：id：{}****",message.getMessageProperties().getDeliveryTag());
//                }
//            }else {
//                //处理错误消息，拒觉错误消息重新入队
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
//                channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
//                log.info("***消息拒绝***");
//            }
//        });
//        */
//        return container;
//    }
//
//
//    /**
//     * 手动ack全局配置（暂时不配置）
//     * @return
//     */
//    //@Bean
//    public MessageListener channelAwareMessageListener() {
//        ChannelAwareMessageListener channelAwareMessageListener = new ChannelAwareMessageListener() {
//
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                log.info("====1111接收到消息=====");
//                log.info(new String(message.getBody()));
//                if(message.getMessageProperties().getHeaders().get("error") == null){
//
//                    try {
//                        //手动ack应答
//                        //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了
//                        // 否则消息服务器以为这条消息没处理掉 后续还会在发，true确认所有消费者获得的消息
//                        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
//                        log.info("1111消息消费成功：id：{}",message.getMessageProperties().getDeliveryTag());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        //丢弃这条消息
//                        try {
//                            //最后一个参数是：是否重回队列
//                            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }
//                        log.info("消息消费失败：id：{}",message.getMessageProperties().getDeliveryTag());
//                    }
//                }else {
//                    //处理错误消息，拒觉错误消息重新入队
//                    channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
//                    channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
//                    log.info("消息拒绝");
//                }
//            }
//        };
//        return channelAwareMessageListener;
//    }
}
