package com.kevin.springboot_mq.config.delay;

/**
 * @Description:    延时队列配置信息接口
 * @Author:         Kevin
 * @CreateDate:     2019/4/19 22:00
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/19 22:00
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
public interface DelayKeyInterface {

    //缓冲队列
    String DELAY_QUEUE_NAME = "delay_queue";

    //实际消费队列
    String DELAYMSG_RECEIVE_QUEUE_NAME = "delay_receive_queue";

    //延时交换机
    String DELAY_EXCHANGE = "delay_exchange";

    //延迟消息（缓存队列）路由键
    String DELAY_KEY = "delay_key";

    //缓冲队列中的消息过期时间
    int EXPERI_TIME = 1000*10;
}
