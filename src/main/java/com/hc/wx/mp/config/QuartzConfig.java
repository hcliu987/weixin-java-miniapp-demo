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
        //0 1 0/1 ? * *
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 1 0/1 ? * *");


        return TriggerBuilder.newTrigger()
                .startAt(DateBuilder.todayAt(0, 1, 0))
                //  .endAt(DateBuilder.todayAt(9,0,0))
                .forJob(sfJobDetail())
                .withIdentity("redisListReaderTrigger", "group1")
                .withSchedule(cronScheduleBuilder)
                .build();
    }
}
