package com.kevin.springboot_mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootMqApplicationTests {

  // @Autowired
    //private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Test
    public void contextLoads() {
        send();
    }

    public void send(){

        rabbitTemplate.convertAndSend("directExChange","QUEUE_KEY_1","hello world");
        System.out.println("success!");
    }

    /**
     * 接收消息（自动监听）
     * @param msg
     */
    @RabbitHandler
    @RabbitListener(queues = "QUEUE_DIRECT")
    public void receive(String msg){
        System.out.println(msg);
    }
}
