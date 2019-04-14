package com.kevin.springboot_mq.config.fanout;

/**
 * @Description:    DOTO
 * @Author:         Kevin
 * @CreateDate:     2019/4/12 0:24
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/12 0:24
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
public interface FanoutKeyInterface {

    //队列名
    String FANOUT_QUEUE_A_NAME = "fanout_queue_a";

    //队列名
    String FANOUT_QUEUE_B_NAME = "fanout_queue_b";

    //队列名
    String FANOUT_QUEUE_C_NAME = "fanout_queue_c";

    //持久化队列
    String FANOUT_DURABLE_QUEUE_NAME = "fanout_durable_queue";

    //持久化交换机
    String FANOUT_DURABLE_EXCHANGE_NAME = "fanout_durable_exchange";

    //交换机名
    String FANOUT_EXCHANGE_NAME = "fanout_exchange_name";

    //路由键
    String FANOUT_KEY = "fanout_key";
}
