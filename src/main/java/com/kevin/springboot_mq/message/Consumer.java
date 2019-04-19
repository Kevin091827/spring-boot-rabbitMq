package com.kevin.springboot_mq.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kevin.springboot_mq.config.delay.DelayKeyInterface;
import com.kevin.springboot_mq.config.direct.DirectKeyInterface;
import com.kevin.springboot_mq.config.topic.TopicKeyInterface;
import com.kevin.springboot_mq.entity.User;
import com.kevin.springboot_mq.mapper.RegisterDao;
import com.kevin.springboot_mq.utils.AckUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Description:    消费者
 * @Author:         Kevin
 * @CreateDate:     2019/4/11 0:45
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/11 0:45
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
@Component
@Slf4j
public class Consumer {

    /**
     * 接收消息（自动监听）
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = TopicKeyInterface.TOPIC_QUEUE_NAME_A)
    public void recevice(String message){
        log.info("接收到消息：{}",message);
    }

    @Autowired
    private RegisterDao registerDao;


    /**
     * 接收对象信息
     * @param json
     */
    //@RabbitHandler
    //@RabbitListener(queues = DirectKeyInterface.DIRECT_QUEUE_NAME)
    private void receiveObject(Channel channel, String json, Message message,@Headers Map<String,Object> map){

        log.info("接收到对象:{}"+json);
        User user = JSONObject.parseObject(json,User.class);
        registerDao.addUser(user);
        AckUtils.ack(channel,message,map);
    }


    /**
     * 接收删除对象信息
     * @param json
     */
    @RabbitHandler
    @RabbitListener(queues = DirectKeyInterface.DIRECT_QUEUE_NAME)
    public void receiveObjectDel(Channel channel, String json, Message message,@Headers Map<String,Object> map){

        log.info("接收到的id："+json);
        int id = Integer.parseInt(json);
        registerDao.deleteUser(id);
        //<P>代码为在消费者中开启消息接收确认的手动ack</p>
        //<H>配置完成</H>
        //<P>可以开启全局配置</p>
        AckUtils.ack(channel,message,map);
    }

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
}
