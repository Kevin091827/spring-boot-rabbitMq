package com.kevin.springboot_mq.config.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * @Description:    DOTO
 * @Author:         Kevin
 * @CreateDate:     2019/4/12 0:23
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/12 0:23
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
@Configuration
public class DirectConfig {

    /**
     * 设置队列
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(DirectKeyInterface.DIRECT_QUEUE_NAME);
    }

    /**
     * 设置持久化队列
     * @return
     */
    @Bean
    public Queue durableQueue(){
        return new Queue(DirectKeyInterface.DIRECT_DURABLE_QUEUE_NAME,true,true,false);
    }


    /**
     * 设置持久化交换机
     * @return
     */
    @Bean
    public DirectExchange directExchangeDurable(){
        return new DirectExchange(DirectKeyInterface.DIRECT_DURABLE_EXCHANGE_NAME,true,false);
    }

    /**
     * 设置交换机
     * @return
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(DirectKeyInterface.DIRECT_EXCHANGE_NAME);
    }

    /**
     * 在fanout模式下绑定交换机，队列，路由键
     * @param directExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding binding_direct(DirectExchange directExchange,Queue queue){
        return BindingBuilder.bind(queue)
                             .to(directExchange)
                             .with(DirectKeyInterface.DIRECT_KEY);
    }

    @Bean
    public Binding binding_direct_Durable(DirectExchange directExchangeDurable,Queue durableQueue){

        return BindingBuilder.bind(durableQueue)
                             .to(directExchangeDurable)
                             .with(DirectKeyInterface.DIRECT_KEY);
    }

}
