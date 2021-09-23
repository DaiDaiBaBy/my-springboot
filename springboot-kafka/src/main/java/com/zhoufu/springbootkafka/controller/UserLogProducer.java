package com.zhoufu.springbootkafka.controller;

import com.alibaba.fastjson.JSON;
import com.zhoufu.springbootkafka.entity.UserLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: zhoufu
 * @Date: 2021/2/4 17:51
 * @description: 定义 发送信息实体 （消息的发送直接使用 kafkaTemplate 模板即可， 都是封装好了的）
 */
@Component
@Slf4j
public class UserLogProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 发送 数据
     * @param userid
     */
    public void sendLog(String userid){
        UserLog userLog = new UserLog();
        userLog.setUsername("娃哈哈");
        userLog.setUserid(userid);
        userLog.setState("0");
        log.info("kafka 发送用户日志数据：{}", JSON.toJSONString(userLog));
        System.err.println("发送用户日志数据:"+userLog);
        kafkaTemplate.send("user-log", JSON.toJSONString(userLog));
    }
}
