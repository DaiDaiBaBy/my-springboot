//package com.zhoufu.springboottask.task;
//
//import com.zhoufu.springboottask.service.TaskService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * @Author: zhoufu
// * @Date: 2021/3/3 9:22
// * @description:  定时任务
// */
//@Component
//@Slf4j
//public class DemoScheduling {
//    @Autowired
//    private TaskService taskService;
//
////    @Scheduled(cron = "${scheduled.demo}")
//    public void demoScheduling(){
//        log.info("一个小时一次的查数据定时任务==========demo");
//        taskService.demoAsync();
//    }
//}
