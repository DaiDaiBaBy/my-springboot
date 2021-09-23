package com.zhoufu.springbootrocketmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @Author: zhoufu
 * @Date: 2021/2/26 17:32
 * @description:  同步消费者 1
 */
@Slf4j
@Service
@RocketMQMessageListener(topic = "string-topic", consumerGroup = "my-consumer_string-topic")
public class SyncGeneralMQConsumer1 implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        log.info("SyncGeneralMQConsumer1 端接收到消息：{}", s);
    }
    /**
     * RocketMQ 在设计的时候就不希望一个消费者同时处理多个类型的消息，
     *  因此一个consumerGroup下面的consumer的职责应该是一样的，
     *  不要干不同的事(即消费不同的 topic)， 建议consumerGroup 与 topic 一一对应
     */
}
