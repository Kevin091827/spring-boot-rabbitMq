package com.kevin.springboot_mq.message;

import com.alibaba.fastjson.JSONObject;
import com.kevin.springboot_mq.config.direct.DirectKeyInterface;
import com.kevin.springboot_mq.config.topic.TopicKeyInterface;
import com.kevin.springboot_mq.entity.User;
import com.kevin.springboot_mq.mapper.RegisterDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    @RabbitHandler
    @RabbitListener(queues = DirectKeyInterface.DIRECT_QUEUE_NAME)
    private void receiveObject(String json){
        log.info("接收到对象:{}"+json);
        registerDao.addUser(json.toString());
    }

}
