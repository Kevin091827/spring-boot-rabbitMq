package com.kevin.springboot_mq;

import com.kevin.springboot_mq.config.delay.DelayKeyInterface;
import com.kevin.springboot_mq.utils.AckUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootMqApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void contextLoads() {
        send(123456);
    }

    public void send(int delayTimes){

        //发送消息到延迟实际消费队列
        rabbitTemplate.convertAndSend(DelayKeyInterface.DELAY_EXCHANGE,DelayKeyInterface.DELAY_KEY,"hello");
        System.out.println("success!");
    }

    /**
     * 接收消息（自动监听）
     * @param msg
     */
    @RabbitHandler
    @RabbitListener(queues = DelayKeyInterface.DELAYMSG_RECEIVE_QUEUE_NAME)
    public void receive(String msg, Channel channel, Message message, @Headers Map<String,Object> map){
        System.out.println( LocalDateTime.now()+msg);
        //手动ack消息确认
        AckUtils.ack(channel,message,map);
    }
}
