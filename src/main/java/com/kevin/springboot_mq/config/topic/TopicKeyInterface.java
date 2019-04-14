package com.kevin.springboot_mq.config.topic;

/**
 * @Description:    DOTO
 * @Author:         Kevin
 * @CreateDate:     2019/4/12 17:25
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/12 17:25
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
public interface TopicKeyInterface {

    //队列名
    String TOPIC_QUEUE_NAME_A = "TOPIC_QUEUE_A";

    //队列名
    String TOPIC_QUEUE_NAME_B = "TOPIC_QUEUE_B";

    //队列名
    String TOPIC_QUEUE_NAME_C = "TOPIC_QUEUE_C";

    //持久化队列名
    String TOPIC_DURABLE_QUEUE_NAME = "topic_DURABLE_QUEUE";

    //持久化交换机名
    String TOPIC_DURABLE_EXCHANGE_NAME = "topic_DURABLE_EXCHANGE";

    //交换机名
    String TOPIC_EXCHANGE_NAME = "TOPIC_EXCHANGE";

    //广播路由键
    String TOPIC_KEY = "TOPIC_#";

    //队列路由键
    String TOPIC_KEY_A = "TOPIC_A";

    //队列路由键
    String TOPIC_KEY_B = "TOPIC_B";

    //队列路由键
    String TOPIC_KEY_C = "TOPIC_C";
}
