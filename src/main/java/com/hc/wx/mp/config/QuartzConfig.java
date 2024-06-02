package com.hc.wx.mp.config;

import com.hc.wx.mp.task.SFTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail sfJobDetail() {
        return JobBuilder.newJob(SFTask.class)
                .withIdentity("sfTask")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger sfJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInHours(1) // 每小时执行一次
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .startAt(DateBuilder.todayAt(0,0,0))
                .forJob(sfJobDetail())
                .withIdentity("redisListReaderTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
