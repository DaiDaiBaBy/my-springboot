package com.zhoufu.springbootasync;

import com.zhoufu.springbootasync.demo.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.Future;

@SpringBootTest
class SpringbootAsyncApplicationTests {
    @Autowired
    private Task task;
    @Test
    void contextLoads() throws Exception {
        long start = System.currentTimeMillis();
        Future<String> one = task.doTaskOne();
        Future<String> two = task.doTaskTwo();
        Future<String> three = task.doTaskThree();

        while (true) {
            if (one.isDone() && two.isDone() && three.isDone()) {
                // 都完成，退出
                break;
            }
            Thread.sleep(1000);
        }
        long end = System.currentTimeMillis();
        System.out.println("完成任务，总耗时：" + (end - start) + "毫秒");
    }

}
