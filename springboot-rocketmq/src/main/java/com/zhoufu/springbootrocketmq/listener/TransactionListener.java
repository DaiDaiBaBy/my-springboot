package com.zhoufu.springbootrocketmq.listener;

import com.zhoufu.springbootrocketmq.entity.OrderPaidEventTx;
import com.zhoufu.springbootrocketmq.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhoufu
 * @Date: 2021/2/26 10:08
 * @description:  事务消息生产者端的消息监听器   实现的是 RocketMQLocalTransactionListener
 * 重写执行本地事务的方法 和检查本地事务方法
 */
@Slf4j
@RocketMQTransactionListener(txProducerGroup = "test-txProducerGroup-name")
public class TransactionListener implements RocketMQLocalTransactionListener {
    /*
     * RocketMQLocalTransactionState说明：
     *      COMMIE：
     *          事务消息提交， 会被正确分发给消费者，（事务消息 先发送到broker(中间商), 对消费端不可见，为UNKNOWNN 状态， 在这里
     *          回调的时候如果返回COMMIT，那么。  消费端马上就能收到消息了）
     *      ROLLBACK：
     *          该状态表示该事物消息被回滚，因为本地事务逻辑执行失败导致，(如数据库异常、或者SQL异常，或者其他异常，
     *          这时候消息在 broker中就会被删除掉， 不再会发送到 consume消费者)
     *      UNKNOWN：
     *          表示事务消息还未确定，可能是业务方执行本地逻辑事务时间耗时过长或者网络原因导致的， 该状态会导致broker对事物消息进行回查，
     *          默认回查总次数15次，第一次回查时间间隔是6s， 后续每次的间隔为60s， （消息一旦发送，状态就为中间状态： UNKNOWN）
     *
     */
    /**
     * 执行本地的事务逻辑
     * @param message
     * @param arg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object arg) {
        log.info("执行本地事务逻辑：{}", message);
        CountDownLatch latch = null;
        try {
            latch = (CountDownLatch) arg;
            byte[] body = (byte[]) message.getPayload();
            String json = new String(body, RemotingHelper.DEFAULT_CHARSET);
            OrderPaidEventTx orderPaidEventTx = JsonUtil.jsonToObject(json, OrderPaidEventTx.class);

            // 执行本地事务， 比如下单成功，存储订单信息
            assert orderPaidEventTx != null;
            log.info("执行本地事务接收到的消息内容为：{}", orderPaidEventTx.toString());

            // 模拟现实中的 业务逻辑
            TimeUnit.SECONDS.sleep(2);

            // 本地事务执行成功
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e){
            e.printStackTrace();
            log.error("本地事务执行失败：如数据库异常、sql异常或者其他异常，异常原因：{}", e.getMessage());
            return RocketMQLocalTransactionState.ROLLBACK;
        } finally {
            if (latch != null){
                // 提交事务完成， 或代码执行完成一定要把CountDownLatch 归0 ， 不然会造成主线程阻塞
                latch.countDown();
            }
        }
    }

    /**
     * 执行  事务回查逻辑
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        /*
         * 6s  本地事务还没执行完成，就会触发回调检查的方法
         *
         * 这时候就要做自己对这个订单的异常处理， 最后根据处理的情况来决定
         * 重新发送这个订单到消费者、 还是删除 、还是继续回查
         */

//        return RocketMQLocalTransactionState.COMMIT;  重新发送消息到消费者
//        return RocketMQLocalTransactionState.ROLLBACK; 消息在broker删除， 不会发送到消费端
        return null;
    }
}
