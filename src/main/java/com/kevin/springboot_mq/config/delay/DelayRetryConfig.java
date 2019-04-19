package com.kevin.springboot_mq.config.delay;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:    延迟重试配置类
 * @Author:         Kevin
 * @CreateDate:     2019/4/20 1:22
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/20 1:22
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
@Configuration
public class DelayRetryConfig {

    /**
     * 重试队列
     * @return
     */
    @Bean
    public Queue retryQueue(){
        return new Queue(DelayRetryKeyInterface.RETRY_QUEUE);
    }

    /**
     * 重试交换机
     * @return
     */
    @Bean
    public DirectExchange retryExchange(){
        return new DirectExchange(DelayRetryKeyInterface.RETRY_EXCHANGE);
    }

    /**
     * 绑定重试队列，交换机，路由键
     * @return
     */
    @Bean
    public Binding bindingRetry(){
        return BindingBuilder.bind(retryQueue()).to(retryExchange()).with(DelayRetryKeyInterface.RETRY_KEY);
    }


    /**
     * 实际队列
     * @return
     */
    @Bean
    public Queue consumeQueue(){
        return new Queue(DelayRetryKeyInterface.RETRY_CONSUME_QUEUE);
    }

    /**
     * 实际交换机
     * @return
     */
    @Bean
    public DirectExchange consumeExchange(){
        return new DirectExchange(DelayRetryKeyInterface.RETRY_CONSUME_EXCHANGE);
    }

    /**
     * 绑定实际队列，交换机，路由键
     * @return
     */
    @Bean
    public Binding bindingConsume(){
        return BindingBuilder.bind(consumeQueue()).to(consumeExchange()).with(DelayRetryKeyInterface.CONSUME_KEY);
    }

}
