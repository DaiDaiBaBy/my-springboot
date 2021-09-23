package com.zhoufu.springbootrocketmq.consumer;

import com.zhoufu.springbootrocketmq.entity.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @Author: zhoufu
 * @Date: 2021/2/26 17:52
 * @description:
 */
@Slf4j
@Service
@RocketMQMessageListener(consumeMode = ConsumeMode.CONCURRENTLY, selectorType = SelectorType.TAG, topic = "selector-topic",
        consumerGroup = "my-consumer-nxwl_selector_topic", selectorExpression = "tag2")
public class SelectorMQConsumer2 implements RocketMQListener<OrderPaidEvent> {
    @Override
    public void onMessage(OrderPaidEvent orderPaidEvent) {
        log.info("tag2======> 消息过滤消费端接收到消息：{}", orderPaidEvent.toString());
    }
}
