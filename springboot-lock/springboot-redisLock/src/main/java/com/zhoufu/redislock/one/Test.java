package com.zhoufu.redislock.one;

/**
 * @Author: zhoufu
 * @Date: 2021/3/5 11:43
 * @description: main 测试
 */
public class Test {
    public static void main(String[] args) {
        Service service = new Service();
        for (int i = 0; i < 50; i ++){
            ThreadA threadA = new ThreadA(service);
            threadA.start();
        }
    }
}
