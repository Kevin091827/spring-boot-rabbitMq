package com.kevin.springboot_mq.config.direct;

/**
 * @Description:    配置交换机名，队列名等常量
 * @Author:         Kevin
 * @CreateDate:     2019/4/11 23:08
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/11 23:08
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
public interface DirectKeyInterface {

    //交换机名
    String DIRECT_EXCHANGE_NAME = "direct_exchange";

    //持久化交换机
    String DIRECT_DURABLE_EXCHANGE_NAME = "direct_durable_exchange";

    //持久化队列
    String DIRECT_DURABLE_QUEUE_NAME = "direct_durable_queue";

    //队列名
    String DIRECT_QUEUE_NAME = "direct_queue";

    //路由键
    String DIRECT_KEY = "direct_key";


}
