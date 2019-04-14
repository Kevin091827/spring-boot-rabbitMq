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
     * 设置持久化topic模式队列
     * durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
     * exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
     * autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
     * @return
     */
    @Bean
    public Queue durableTopicQueue(){
        return new Queue(TopicKeyInterface.TOPIC_DURABLE_QUEUE_NAME,true,true,false);
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
     * 设置持久化交换机
     * durable:
     * autoDelete:
     * @return
     */
    @Bean
    public TopicExchange durableTopicExchange(){
        return new TopicExchange(TopicKeyInterface.TOPIC_DURABLE_QUEUE_NAME,true,false);
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

    /**
     * 绑定交换机队列
     * @param durableTopicQueue
     * @param durableTopicExchange
     * @return
     */
    @Bean
    public Binding bindingTopicQueueDurable(Queue durableTopicQueue,TopicExchange durableTopicExchange){
        return BindingBuilder.bind(durableTopicQueue).to(durableTopicExchange).with(TopicKeyInterface.TOPIC_KEY_C);
    }
}
