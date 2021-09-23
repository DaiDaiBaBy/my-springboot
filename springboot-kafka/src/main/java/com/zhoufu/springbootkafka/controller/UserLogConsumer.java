package com.zhoufu.springbootkafka.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author: zhoufu
 * @Date: 2021/2/4 17:55
 * @description: 获取消息的消费者实体
 * 注： 消费者机制是通过 监听器实现的，  直接使用 @KafkaListener(topics = {""}) 注解，根据指定的条件进行消息的监听
 */
@Component
@Slf4j
public class UserLogConsumer {

    /**
     *
     * @param consumerRecord
     */
    @KafkaListener(topics = "{user-log}")
    public void consumer(ConsumerRecord<?,?> consumerRecord){
        // 判断是否为null
        Optional<?> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        log.info(">>>>>>>>>> record : {}", kafkaMessage);
        System.out.println(">>>>>>>>>> record = " + kafkaMessage);
        if (kafkaMessage.isPresent()) {
            // 得到Optional 实例中的值
            Object message = kafkaMessage.get();
            log.info("kafka 消费信息：{}", message);
            System.out.println("消费信息：" + message);
        }
    }
}
