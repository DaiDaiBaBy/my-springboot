package com.zhoufu.springbootrocketmq.controller;

import com.zhoufu.springbootrocketmq.service.GeneralMQMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhoufu
 * @Date: 2021/2/26 18:08
 * @description: 对 RocketMQTemplate 封装原始rocketMQ 一些常用的示例测试
 * rocketmq-spring-boot-starter 源码地址：https://github.com/apache/rocketmq-spring
 */
@RestController
@RequestMapping("/rocketMq")
public class MQMessageController {

    @Autowired
    private GeneralMQMessageService generalMQMessageService;

    /**
     * rocketMq同步消息发送测试
     */
    @RequestMapping("/syncGeneralMQMessageSend")
    public void syncGeneralMQMessageSend() {
        generalMQMessageService.syncGeneralMQMessageSend();
    }

    /**
     * rocketMq异步消息发送测试
     */
    @RequestMapping("/asyncSendMQMessageSend")
    public void asyncSendMQMessageSend() {
        generalMQMessageService.asyncGeneralMQMessageSend();
    }

    /**
     * rocketMq单向发送不关心结果的发送测试【日志收集】
     */
    @RequestMapping("/oneWaySendMQMessageSend")
    public void oneWaySendMQMessageSend() {
        generalMQMessageService.oneWayGeneralMQMessageSend();
    }

    /**
     * 延迟消息发送测试
     */
    @RequestMapping("/delayedSendMQMessageSend")
    public void delayedSendMQMessageSend() {
        generalMQMessageService.delayedGeneralMQMessageSend();
    }

    /**
     * 顺序消息发送
     */
    @RequestMapping("/orderlyMQMessageSend")
    public void orderlyMQMessageSend() {
        generalMQMessageService.orderlyGeneralMQMessageSend();
    }

    /**
     * 分布式事务消息发送
     */
    @RequestMapping("/transactionMQSend")
    public void transactionMQSend() {
        generalMQMessageService.transactionGeneralMQMessageSend();
    }

    /**
     * 消息过滤消息发送
     */
    @RequestMapping("/selectorMQSend")
    public void selectorMQSend() {
        generalMQMessageService.selectorGeneralMQMessageSend();
    }
}
