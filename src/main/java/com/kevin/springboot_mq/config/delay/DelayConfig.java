package com.kevin.springboot_mq.config.delay;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:    延时队列配置类
 * @Author:         Kevin
 * @CreateDate:     2019/4/19 22:05
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/19 22:05
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
@Configuration
public class DelayConfig {

    /**
     * 延时交换机 --- 交换机用于重新分配队列（接收死信队列中的过期消息，将其转发到需要延迟消息的模块队列）
     * @return
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(DelayKeyInterface.DELAY_EXCHANGE);
    }

    /**
     * 实际消费队列
     * 用于延时消费的队列
     */
    @Bean
    public Queue repeatTradeQueue() {
        Queue queue = new Queue(DelayKeyInterface.DELAYMSG_RECEIVE_QUEUE_NAME,true,false,false);
        return queue;
    }

    /**
     * 绑定交换机并指定routing key（死信队列绑定延迟交换机和实际消费队列绑定延迟交换机的路由键一致）
     * @return
     */
    @Bean
    public Binding  repeatTradeBinding() {
        return BindingBuilder.bind(repeatTradeQueue()).to(exchange()).with(DelayKeyInterface.DELAY_KEY);
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue deadLetterQueue() {
        Map<String,Object> args = new HashMap<>();
        //消息过期时间
        args.put("x-message-ttl", DelayKeyInterface.EXPERI_TIME);
        //消息过期后发到指定交换机
        args.put("x-dead-letter-exchange", DelayKeyInterface.DELAY_EXCHANGE);
        //消息过期后附带指定路由键发到指定交换机上
        args.put("x-dead-letter-routing-key", DelayKeyInterface.DELAY_KEY);
        return new Queue(DelayKeyInterface.DELAY_QUEUE_NAME, true, false, false, args);
    }

}
