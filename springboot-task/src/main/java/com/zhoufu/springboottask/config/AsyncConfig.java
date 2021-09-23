package com.zhoufu.springboottask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: zhoufu
 * @Date: 2021/3/2 17:40
 * @description:  异步方法线程池配置类
 */
@Configuration
public class AsyncConfig {

    /**
     *  处理异步方法自定义 线程池配置
     * @return
     */
    @Bean("doExecutor")
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10); // 核心线程数
        taskExecutor.setMaxPoolSize(20); // 最大核心线程数
        taskExecutor.setKeepAliveSeconds(300); // 线程最大空闲时间
        taskExecutor.setQueueCapacity(1000); // 队列阻塞长度
        taskExecutor.setThreadNamePrefix("AsyncExecutorThread--"); // 新建线程名称
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.initialize();
        return taskExecutor;
    }
}
