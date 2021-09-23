package com.zhoufu.springboottask.task;
import com.zhoufu.springboottask.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: zhoufu
 * @Date: 2021/3/5 14:51
 * @description:
 */
@Slf4j
@Component
@EnableScheduling
public class TaskService implements SchedulingConfigurer {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ?");
    private Runnable runnable = () -> {
        // 定时任务的内容
        System.out.println("开始执行定时任务》》》》");
    };
    private Trigger trigger = triggerContext -> {
        // 获取 cron
        String cron = sdf.format(DateUtils.addDateSeconds(new Date(), 15));
        System.out.println("cron: " + cron);
        return new CronTrigger(cron).nextExecutionTime(triggerContext);
    };

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(runnable, trigger);
    }
}
