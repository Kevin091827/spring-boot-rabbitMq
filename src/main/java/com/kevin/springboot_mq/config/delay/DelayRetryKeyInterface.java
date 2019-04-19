package com.kevin.springboot_mq.config.delay;

/**
 * @Description:    延迟重试配置信息接口
 * @Author:         Kevin
 * @CreateDate:     2019/4/20 1:21
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/20 1:21
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
public interface DelayRetryKeyInterface {

    //重试队列(缓冲队列)
    String RETRY_QUEUE = "retry_queue";

    //实际队列
    String RETRY_CONSUME_QUEUE = "retry_consume_queue";

    //实际交换机
    String RETRY_CONSUME_EXCHANGE = "retry_consume_exchange";

    //重试交换机
    String RETRY_EXCHANGE = "retry_exchange";

    //实际路由键
    String CONSUME_KEY = "consume_key";

    //重试路由键
    String RETRY_KEY = "retry_key";
}
