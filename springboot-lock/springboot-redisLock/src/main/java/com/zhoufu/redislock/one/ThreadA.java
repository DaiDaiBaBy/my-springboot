package com.zhoufu.redislock.one;

/**
 * @Author: zhoufu
 * @Date: 2021/3/5 11:40
 * @description:  模拟线程进行秒杀服务
 */
public class ThreadA extends Thread{
    private Service service;
    public ThreadA(Service service){
        this.service = service;
    }


    @Override
    public void run() {
        service.seckill();
    }
}
