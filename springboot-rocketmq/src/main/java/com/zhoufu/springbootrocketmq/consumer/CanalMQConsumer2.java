package com.zhoufu.springbootrocketmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @Author: zhoufu
 * @Date: 2021/2/26 17:56
 * @description:
 */

@Slf4j
@Service
@RocketMQMessageListener(consumeMode = ConsumeMode.ORDERLY, topic = "united_data_sync_topic", consumerGroup = "my_test_group2")
public class CanalMQConsumer2 implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        try {
            String masg = new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET);
            log.info(" canal united ======> {}", masg);
        } catch (Exception e){
            e.printStackTrace();
            log.error("", e);
        }
    }
    /* MessageExt包括各种烦属性字段：
     * brokerName：broker名称
        queueId：记录MessageQueue编号，消息在Topic下对应的MessageQueue中被拉取
        storeSize：记录消息在Broker存盘大小
        queueOffset：记录在ConsumeQueue中的偏移
        sysFlag：记录一些系统标志的开关状态，MessageSysFlag中定义了系统标识
        bornTimestamp：消息创建时间，在Producer发送消息时设置
        bornHost：记录发送改消息的producer地址
        storeTimestamp：消息存储时间
        storeHost：记录存储该消息的Broker地址
        msgId：消息Id
        commitLogOffest：记录消息在Broker中存储偏移
        bodyCRC：消息内容CRC校验值
        reconsumeTimes：消息重试消费次数
        body：Producer发送的实际消息内容，以字节数组（ASCII码）形式进行存储。Message消息有一定大小限制。
        transactionId：事务消息相关的事务编号
     *
     */
}
