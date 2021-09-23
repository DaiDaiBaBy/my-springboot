package com.zhoufu.springbootrocketmq.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: zhoufu
 * @Date: 2021/2/26 9:54
 * @description: 分布式事务消息体
 */
@Data
public class OrderPaidEventTx implements Serializable {
    private static final long serialVersionUID = 7151130443947141904L;

    private String orderId;

    private BigDecimal paidMoney;
}
