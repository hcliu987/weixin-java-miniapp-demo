package com.hc.wx.mp.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Job {
    TaskRunner taskRunner = new TaskRunner();

    public static void main(String[] args) throws SchedulerException {
        List myNumbers = new ArrayList<int[]>();
        myNumbers.add(new int[]{3, 15, 16, 21, 26, 32, 5});
        myNumbers.add(new int[]{1, 14, 16, 22, 26, 31, 12});
        myNumbers.add(new int[]{4, 13, 17, 27, 29, 30, 6});
        myNumbers.add(new int[]{4, 9, 11, 13, 27, 28, 11});
        myNumbers.add(new int[]{7, 11, 20, 23, 25, 32, 1});

        JobDataMap jobDataMap=new JobDataMap();
        jobDataMap.put("param",myNumbers);
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class).withIdentity("job", "jobGroup")
            .usingJobData(jobDataMap)
            .build();
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger", "group")
            .withSchedule(CronScheduleBuilder.cronSchedule("0/1  * * ? * 1,3,5"))
            .build();
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.scheduleJob(jobDetail,trigger);

        scheduler.start();


    }
}
