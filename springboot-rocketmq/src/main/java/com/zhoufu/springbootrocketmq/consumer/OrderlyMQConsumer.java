package com.zhoufu.springbootrocketmq.consumer;

import com.zhoufu.springbootrocketmq.entity.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @Author: zhoufu
 * @Date: 2021/2/26 17:46
 * @description:  顺序消息消费者
 */
@Service
@Slf4j
@RocketMQMessageListener(consumeMode = ConsumeMode.ORDERLY, topic = "orderly-topic", consumerGroup = "my-consumer_orderly-topic")
public class OrderlyMQConsumer implements RocketMQListener<OrderPaidEvent> {
    @Override
    public void onMessage(OrderPaidEvent orderPaidEvent) {
        log.info("MQ 顺序测试消费端接收到消息：{}", orderPaidEvent.toString());
    }
    /*
     * RocketMQ 在设计的时候就不希望一个消费者同时处理多个类型的消息，
     *  因此一个consumerGroup下面的consumer的职责应该是一样的，
     *  不要干不同的事(即消费不同的 topic)， 建议consumerGroup 与 topic 一一对应
     *
     *  顺序消费消费者需要设置为:consumeMode : ConsumeMode.ORDERLY
     *  注意：消费者默认实现的接口为MessageListenerConcurrently， 对应为ConsumeMode.CONCURRENTLY
     *  消费者实现MessageListenerOrderly的时候，就是为顺序消费。
     */
}
