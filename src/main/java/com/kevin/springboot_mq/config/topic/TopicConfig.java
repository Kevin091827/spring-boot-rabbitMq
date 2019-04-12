package com.kevin.springboot_mq.config.topic;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {

    /**
     * 设置队列
     * @return
     */
    @Bean
    public Queue topicQueueA(){
        return new Queue(TopicKeyInterface.TOPIC_QUEUE_NAME_A);
    }

    /**
     * 设置队列
     * @return
     */
    @Bean
    public Queue topicQueueB(){
        return new Queue(TopicKeyInterface.TOPIC_QUEUE_NAME_B);
    }

    /**
     * 设置队列
     * @return
     */
    @Bean
    public Queue topicQueueC(){
        return new Queue(TopicKeyInterface.TOPIC_QUEUE_NAME_C);
    }

    /**
     * 设置交换机
     * @return
     */
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TopicKeyInterface.TOPIC_EXCHANGE_NAME);
    }

    /**
     * 在topic模式下，绑定交换机，队列和路由键
     * @return
     */
    @Bean
    public Binding bindingTopicQueueA(Queue topicQueueA,TopicExchange topicExchange){
        return BindingBuilder.bind(topicQueueA).to(topicExchange).with(TopicKeyInterface.TOPIC_KEY);
    }

    /**
     * 在topic模式下，绑定交换机，队列和路由键
     * @return
     */
    @Bean
    public Binding bindingTopicQueueB(Queue topicQueueB,TopicExchange topicExchange){
        return BindingBuilder.bind(topicQueueB).to(topicExchange).with(TopicKeyInterface.TOPIC_KEY);
    }

    /**
     * 在topic模式下，绑定交换机，队列和路由键
     * @return
     */
    @Bean
    public Binding bindingTopicQueueC(Queue topicQueueC,TopicExchange topicExchange){
        return BindingBuilder.bind(topicQueueC).to(topicExchange).with(TopicKeyInterface.TOPIC_KEY_C);
    }
}
