package com.zhoufu.springbootkafka.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @Author: zhoufu
 * @Date: 2021/2/4 17:49
 * @description:   定义 kafak 数据传输对象
 */
@Component
@Data
public class UserLog {
    private String username;
    private String userid;
    private String state;
}
