package com.zhoufu.springbootrocketmq.service.impl;

import com.zhoufu.springbootrocketmq.constant.MessageDelayConstant;
import com.zhoufu.springbootrocketmq.entity.OrderPaidEvent;
import com.zhoufu.springbootrocketmq.entity.OrderPaidEventTx;
import com.zhoufu.springbootrocketmq.service.GeneralMQMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: zhoufu
 * @Date: 2021/2/26 14:07
 * @description:
 */
@Slf4j
@Service
public class GeneralMQMessageServiceImpl implements GeneralMQMessageService {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 同步 消息发送方式：
     *      指消息发送方发出数据包后，会在收到接收方发出响应之后才发出下一个数据包的的通讯方式
     */
    @Override
    public void syncGeneralMQMessageSend() {
        // 发送string类型的消息 ： SendResult syncSend(String destination, Object payload);
        // 对应消费者：  SyncGeneralMQConsumer1
        String stringTopic = "string-topic";
        String payloadStr = "Hello World";
        SendResult sendResultStr = rocketMQTemplate.syncSend(stringTopic, payloadStr);
        log.info("MQ 同步发送String类型的消息topic为：{}，返回结果为：{}", stringTopic, sendResultStr);

        // 发送 对象类型的消息： SendResult syncSend(String destination, Message<?> message);
        // 对应消费者：   SyncGeneralMQConsumer2
        String objectTopic = "object-topic";
        OrderPaidEvent orderPaidEvent = new OrderPaidEvent();
        orderPaidEvent.setOrderId("2102260920378928418519");
        orderPaidEvent.setPaidMoney(new BigDecimal(20));

        // 消息构建
        Message<OrderPaidEvent> message = MessageBuilder.withPayload(orderPaidEvent).build();
        Message<OrderPaidEvent> build = MessageBuilder.withPayload(orderPaidEvent).setHeader("", "").build();
        SendResult sendResultObj = rocketMQTemplate.syncSend(objectTopic, message);
        log.info("MQ 同步发送 对象类型（对象放到MessageBuilder构建）的消息topic为：{}，返回结果为：{}", objectTopic, sendResultObj);

        // 发送 对象类型的消息： SendResult syncSend(String destination, Object payload); convertAndSend 也可以发送消息，但是返回值为void
        // 对应消费者： SyncGeneralMQConsumer2
        OrderPaidEvent orderPaidEvent1 = new OrderPaidEvent();
        orderPaidEvent1.setOrderId("21022609203789221413422");
        orderPaidEvent1.setPaidMoney(new BigDecimal(50));
        SendResult sendResultObj1 = rocketMQTemplate.syncSend(objectTopic, orderPaidEvent1);
        log.info("MQ 同步发送 对象类型（直接传入对象）消息topic为：{}， 返回结果为：{}", objectTopic, sendResultObj1);
    }

    /**
     * 异步 消息发送方式：
     *      指的是发送方发出数据后，不用等待接收方发出响应，接着直接发送下一个数据包的通讯方式
     *
     * MQ 的异步发送， 需要用户自己去实现异步发送回调接口（SendCallBack）， 在执行异步发送时，
     * 应用不用等待服务器响应即可直接返回，通过回调接口去接收服务器的响应， 并对服务器的响应结果进行处理
     */
    @Override
    public void asyncGeneralMQMessageSend() {
        String objectTopic = "object-topic";
        OrderPaidEvent orderPaidEvent = new OrderPaidEvent();
        orderPaidEvent.setOrderId("210226092037892214148741");
        orderPaidEvent.setPaidMoney(new BigDecimal(200));
        rocketMQTemplate.asyncSend(objectTopic, orderPaidEvent, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("异步消息发送成功：{}", sendResult);
            }
            @Override
            public void onException(Throwable throwable) {
                log.error("异步消息发送失败：{}", throwable.getCause());
            }
        });
    }

    /**
     *  单向发送消息：
     *      特点： 只负责发送消息，不等待服务器响应 且 没有回调函数触发，即只发送请求不等待应答
     *      这种方式发送消息的过程时耗非常短，一般在微秒级别
     *    应用场景： 适用于某些耗时非常短， 但对可靠性要求不高的场景，比如日志收集
     *
     *    与异步发送消息的区别：
     *            异步其实还是要发送结果， 只不过是回调回来，不阻塞当前线程等待结果
     */
    @Override
    public void oneWayGeneralMQMessageSend() {
        String objectTopic = "object-topic";
        OrderPaidEvent orderPaidEvent = new OrderPaidEvent();
        orderPaidEvent.setOrderId("2102260920123458476543");
        orderPaidEvent.setPaidMoney(new BigDecimal(100));
        rocketMQTemplate.sendOneWay(objectTopic, orderPaidEvent);
    }

    /**
     * 延迟 发送消息：
     *       rocketMQ的延迟消息发送 其实是已发送 就已经到 broker端了， 然后消费者端会延迟收到消息。
     *      rocketMQ 目前只支持固定精度的定时消息：
     *          延迟级别(18个等级):
     *              1到18分别对应1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     *    为什么不支持固定精度的定时消息？
     *          因为rocketMQ之所以性能好，主要是因为broker端做的事情比较少，基本上是交给业务端(消费端)做,
     *          如果支持的话，我们自定义的延迟时候，就会很大程度的降低broker的性能
     *    延迟的底层方法 是使用定时器任务实现的
     *
     */
    @Override
    public void delayedGeneralMQMessageSend() {
        // SendResult syncSend(String destination, Message<?> message, long timeout, int delayLevel);
        String objectTopic = "object-topic";
        OrderPaidEvent orderPaidEvent = new OrderPaidEvent();
        orderPaidEvent.setOrderId("2102262326414928418272");
        orderPaidEvent.setPaidMoney(new BigDecimal(300));
        // 构建消息
        Message<OrderPaidEvent> buildMessage = MessageBuilder.withPayload(orderPaidEvent).build();
        SendResult sendResult = rocketMQTemplate.syncSend(objectTopic, buildMessage, 1000, MessageDelayConstant.TIME_5S);
        log.info("MQ 发送延迟消息 topic为：{}， 返回结果为：{}", objectTopic, sendResult);
    }

    // 顺序发送消息
    @Override
    public void orderlyGeneralMQMessageSend() {
        // public SendResult syncSendOrderly(String destination, Object payload, String hashKey);
        String orderlyTopic = "orderly-topic";

        // 同步顺序发送---对应顺序消费
        for (int i = 0; i< 5; i ++){
            OrderPaidEvent orderPaidEvent = new OrderPaidEvent();
            String orderId = "2102262326414928418272";
            orderPaidEvent.setOrderId(orderId);
            orderPaidEvent.setPaidMoney(new BigDecimal(20));
            orderPaidEvent.setOrderly(i);

            /*使用这个hashKey去选择投递到哪个队列，比如可以设置为:orderId 或则 productId
             *
             * rocketMQ创建topic的时候默认的队列长度为16个，那么这个hashKey，
             *  怎么通过自己设置的orderId或则productId变成队列标示的比如如果16和队列这个值应该在1-16之间？
             *List<MessageQueue> messageQueueList = this.mQClientFactory.getMQAdminImpl().parsePublishMessageQueues(topicPublishInfo.getMessageQueueList());
             * 上面这个方法就能获取这个topic创建的时候的队列长度，然后根据他底层有个取模的方法，取到其中一个队列
             *
             * org.apache.rocketmq.client.producer.MessageQueueSelector 这里面有个实现： SelectMessageQueueByHash
             *      public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg)
             * 所以为什么这个方法的源码说我们的hashkey可以设置为：orderId和productId,这样同样的orderId就可以进入同一个队列 按照顺序消费
             *
             * 顺序消费的底层源码实现就是必须选择一个队列，然后在这个对面里面的消息和生产顺序保持一致。
             */
            SendResult sendResult = rocketMQTemplate.syncSendOrderly(orderlyTopic, orderPaidEvent, orderId);
            log.info("同步顺序发送消息返回结果：{}", sendResult);
        }

        // 异步顺序发送---对应顺序消费
        for (int i = 0 ; i < 5; i ++){
            OrderPaidEvent orderPaidEvent = new OrderPaidEvent();
            String orderId = "1233456789875423871";
            orderPaidEvent.setOrderId(orderId);
            orderPaidEvent.setPaidMoney(new BigDecimal(20));
            orderPaidEvent.setOrderly(i);

            rocketMQTemplate.asyncSend(orderlyTopic, orderPaidEvent, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("异步顺序发送返回结果：{}", sendResult);
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("异步顺序发送消息失败，原因：{}", throwable.getCause());
                }
            });
        }
    }

    /**
     * 分布式事务信息发送
     */
    @Override
    public void transactionGeneralMQMessageSend() {
      // 消息发送回调 ： com.zhoufu.springbootrocketmq.listener.TransactionListener
        String txProducerGroup = "test-txProducerGroup-name";
        // topic : tag
        String destination = "tx-text-topic";

        // 同步阻塞
        CountDownLatch latch = new CountDownLatch(1);
        OrderPaidEventTx orderPaidEventTx = new OrderPaidEventTx();
        orderPaidEventTx.setOrderId("2102262326414928418272");
        orderPaidEventTx.setPaidMoney(new BigDecimal(300));

        String transactionId = UUID.randomUUID().toString();
        Message<OrderPaidEventTx> buildMessage = MessageBuilder
                .withPayload(orderPaidEventTx)
                .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
                .setHeader(RocketMQHeaders.KEYS, orderPaidEventTx.getOrderId())
                .build();
        // TransactionSendResult sendMessageInTransaction(String txProducerGroup, String destination, Message<?> message, Object arg)
        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(txProducerGroup, destination, buildMessage, latch);
        if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)
                && sendResult.getLocalTransactionState().equals(LocalTransactionState.COMMIT_MESSAGE)){
            // 下单成功， 并且消息对消费端可见
            // //在这里可以异步通知上游服务，也可以继续走自己的逻辑，比如有些逻辑必须保证下单和库存成功之后才能走的。
            log.info("消息发送成功，并且本地事务执行成功");
        }
        try {
            latch.await();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 过滤消息发送
     */
    @Override
    public void selectorGeneralMQMessageSend() {
        for (int i = 0; i < 5; i++){
            Random random = new Random();
            int k = random.nextInt(3) % 2;
            OrderPaidEvent orderPaidEvent = new OrderPaidEvent();
            orderPaidEvent.setOrderId("816241741486214123");
            orderPaidEvent.setPaidMoney(new BigDecimal(20));

            String selectorTopic = "selector-topic";
            String tag = k == 0 ? ":tag1" : ":tag2";
            SendResult sendResult = rocketMQTemplate.syncSend(selectorTopic + tag, orderPaidEvent);
            log.info("消息过滤发送成功》》》》topic为：{}，返回结果为：{}，当前k的值为：{}", selectorTopic, sendResult, k);
        }
    }
}
