package com.kevin.springboot_mq.utils;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;

import java.io.IOException;
import java.util.Map;

/**
 * @Description:    手动ACK实现消息确认机制工具类
 * @Author:         Kevin
 * @CreateDate:     2019/4/19 20:34
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/19 20:34
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
@Slf4j
public class AckUtils {

    public static void ack(Channel channel, Message message,Map<String,Object> map){
        if (map.get("error")!= null){
            log.info("错误的消息");
            try {
                //否认消息,拒接该消息重回队列
                channel.basicNack((Long)map.get(AmqpHeaders.DELIVERY_TAG),false,false);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //手动ACK
        //默认情况下如果一个消息被消费者所正确接收则会被从队列中移除
        //如果一个队列没被任何消费者订阅，那么这个队列中的消息会被 Cache（缓存），
        //当有消费者订阅时则会立即发送，当消息被消费者正确接收时，就会被从队列中移除
        try {
            //手动ack应答
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了
            // 否则消息服务器以为这条消息没处理掉 后续还会在发，true确认所有消费者获得的消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            log.info("消息消费成功：id：{}",message.getMessageProperties().getDeliveryTag());
        } catch (IOException e) {
            e.printStackTrace();
            log.info("消息消费失败：id：{}",message.getMessageProperties().getDeliveryTag());
            //丢弃这条消息
            try {
                //最后一个参数是：是否重回队列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
                //拒绝消息
                //channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
                //消息被丢失
                //channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                //消息被重新发送
                //channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
                //多条消息被重新发送
                //channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
