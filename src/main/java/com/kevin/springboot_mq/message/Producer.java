package com.kevin.springboot_mq.message;

import com.kevin.springboot_mq.config.direct.DirectKeyInterface;
import com.kevin.springboot_mq.config.fanout.FanoutKeyInterface;
import com.kevin.springboot_mq.config.topic.TopicKeyInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:    生产者
 * @Author:         Kevin
 * @CreateDate:     2019/4/11 0:44
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/11 0:44
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
@Component
@Slf4j
public class Producer {

    @Autowired
    private RabbitTemplate amqpTemplate;

    /**
     * 消息发送(点对点）
     * @param message
     */
    public void send(String message){
       amqpTemplate.convertAndSend(DirectKeyInterface.DIRECT_EXCHANGE_NAME,DirectKeyInterface.DIRECT_KEY,message);
       log.info("消息发送成功：{}",message);
    }

    /**
     * 消息发送（广播订阅）
     * @param message
     */
    public void sendOfFanout(String message){
        amqpTemplate.convertAndSend(FanoutKeyInterface.FANOUT_EXCHANGE_NAME,FanoutKeyInterface.FANOUT_KEY,message);
        log.info("消息发送成功：{}",message);
    }

    /**
     * 消息发送（消息模糊广播）
     * @param message
     */
    public void sendOfTopic(String message){
        amqpTemplate.convertAndSend(TopicKeyInterface.TOPIC_EXCHANGE_NAME,TopicKeyInterface.TOPIC_KEY,message);
        log.info("消息发送成功：{}",message);
    }

}
