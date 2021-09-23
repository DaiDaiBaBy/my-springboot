package com.zhoufu.springbootrocketmq.service;

/**
 * @Author: zhoufu
 * @Date: 2021/2/26 13:59
 * @description: 普通消息发送示例
 */
public interface GeneralMQMessageService {
    /**
     * rocketMQ 同步发送消息测试
     */
    void  syncGeneralMQMessageSend();

    /**
     * rocketMQ 异步发送消息测试
     */
    void asyncGeneralMQMessageSend();

    /**
     * rocketMQ 单向发送测试
     */
    void oneWayGeneralMQMessageSend();

    /**
     * rocketMQ发送消息延迟测试
     */
    void delayedGeneralMQMessageSend();

    /**
     * rocketMQ 顺序发送消息测试
     */
    void orderlyGeneralMQMessageSend();

    /**
     * rocketMQ 分布式事务发送消息测试
     */
    void transactionGeneralMQMessageSend();

    /**
     * rocketMQ 消息过滤发送测试
     */
    void selectorGeneralMQMessageSend();
}
