package com.zhoufu.springbootrocketmq.consumer;

import com.zhoufu.springbootrocketmq.entity.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @Author: zhoufu
 * @Date: 2021/2/26 17:32
 * @description:  同步消费者 2
 */
@Slf4j
@Service
@RocketMQMessageListener(topic = "object-topic", consumerGroup = "my-consumer_object-topic")
public class SyncGeneralMQConsumer2 implements RocketMQListener<OrderPaidEvent> {
    @Override
    public void onMessage(OrderPaidEvent s) {
        log.info("SyncGeneralMQConsumer2 端接收到消息：{}", s.toString());
    }
    /*
     * RocketMQ 在设计的时候就不希望一个消费者同时处理多个类型的消息，
     *  因此一个consumerGroup下面的consumer的职责应该是一样的，
     *  不要干不同的事(即消费不同的 topic)， 建议consumerGroup 与 topic 一一对应
     */
}
