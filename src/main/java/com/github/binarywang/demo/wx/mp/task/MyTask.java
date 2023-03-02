package com.github.binarywang.demo.wx.mp.task;

import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

public class MyTask implements SchedulingConfigurer {
    TaskRunner task = new TaskRunner();

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(() -> task);
    }
}
