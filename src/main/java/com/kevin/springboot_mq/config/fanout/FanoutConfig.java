package com.kevin.springboot_mq.config.fanout;

import org.springframework.amqp.core.*;
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
public class FanoutConfig {

    /**
     * 设置队列
     * @return
     */
    @Bean
    public Queue fanoutQueueA(){
        return new Queue(FanoutKeyInterface.FANOUT_QUEUE_A_NAME);
    }

    /**
     * 设置队列
     * @return
     */
    @Bean
    public Queue fanoutQueueB(){
        return new Queue(FanoutKeyInterface.FANOUT_QUEUE_B_NAME);
    }

    /**
     * 设置队列
     * @return
     */
    @Bean
    public Queue fanoutQueueC(){
        return new Queue(FanoutKeyInterface.FANOUT_QUEUE_C_NAME);
    }

    /**
     * 设定交换机
     * @return
     */
    @Bean
    public Exchange fanoutExChange(){
        return new FanoutExchange(FanoutKeyInterface.FANOUT_EXCHANGE_NAME);
    }

    /**
     * 绑定队列和交换机
     * @param fanoutExChange
     * @param fanoutQueueA
     * @return
     */
    @Bean
    public Binding bindingA(FanoutExchange fanoutExChange,Queue fanoutQueueA){
        return BindingBuilder.bind(fanoutQueueA).to(fanoutExChange);
    }

    /**
     * 绑定队列和交换机
     * @param fanoutExChange
     * @param fanoutQueueB
     * @return
     */
    @Bean
    public Binding bindingB(FanoutExchange fanoutExChange,Queue fanoutQueueB){
        return BindingBuilder.bind(fanoutQueueB).to(fanoutExChange);
    }

    /**
     * 绑定队列和交换机
     * @param fanoutExChange
     * @param fanoutQueueC
     * @return
     */
    @Bean
    public Binding bindingC(FanoutExchange fanoutExChange,Queue fanoutQueueC){
        return BindingBuilder.bind(fanoutQueueC).to(fanoutExChange);
    }
}
