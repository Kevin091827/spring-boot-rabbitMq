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
     * 设置持久化fanout模式队列
     * durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
     * exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
     * autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
     * @return
     */
    @Bean
    public Queue durableFanoutQueue(){
        return new Queue(FanoutKeyInterface.FANOUT_DURABLE_QUEUE_NAME,true,true,false);
    }

    /**
     * 设置持久化交换机
     * @return
     */
    @Bean
    public FanoutExchange durableFanoutExchange(){
        return new FanoutExchange(FanoutKeyInterface.FANOUT_DURABLE_EXCHANGE_NAME,true,false);
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

    /**
     * 绑定持久化交换机
     * @param durableFanoutExchange
     * @param durableFanoutQueue
     * @return
     */
    @Bean
    public Binding bindingDurableFanout(FanoutExchange durableFanoutExchange,Queue durableFanoutQueue){
        return BindingBuilder.bind(durableFanoutQueue).to(durableFanoutExchange);
    }
}
