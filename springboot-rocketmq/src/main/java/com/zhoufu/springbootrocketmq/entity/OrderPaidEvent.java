package com.zhoufu.springbootrocketmq.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: zhoufu
 * @Date: 2021/2/26 9:50
 * @description: 消息发送实体
 */
@Data
public class OrderPaidEvent implements Serializable{
    private static final long serialVersionUID = 232239804171266482L;

    private String orderId;

    private BigDecimal paidMoney;

    private Integer orderly;
}
