package com.kevin.springboot_mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @Description:    发送到交换机上失败回调
 * @Author:         Kevin
 * @CreateDate:     2019/4/14 12:04
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/14 12:04
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
@Slf4j
public class RabbitConfirmCallback implements RabbitTemplate.ConfirmCallback{

    /**
     * 发送到交换机上失败回调
     * 消息发送回调(判断是否发送到相应的交换机上)
     * @param correlationData   消息唯一标识
     * @param ack               消息确认结果
     * @param cause             失败原因
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
