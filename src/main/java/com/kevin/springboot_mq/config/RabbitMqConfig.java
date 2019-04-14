package com.kevin.springboot_mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
public class RabbitMqConfig  {

    @Autowired
    private RabbitTemplate rabbitTemplate;

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

}
