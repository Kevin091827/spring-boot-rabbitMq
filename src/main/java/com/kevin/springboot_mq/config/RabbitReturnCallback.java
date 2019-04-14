package com.kevin.springboot_mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @Description:    消息失败后回调
 * @Author:         Kevin
 * @CreateDate:     2019/4/14 11:52
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/14 11:52
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
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
