package com.hc.wx.mp.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.Job;

import java.util.ArrayList;

@Slf4j
public class MyJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        ArrayList<int []> myMunbers = (ArrayList<int[]>) jobDataMap.get("param");
        System.out.println("定时任务开始执行");
        System.out.println(myMunbers.size());
        try {
            context.getScheduler().pauseAll();

        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                context.getScheduler().deleteJob(context.getJobDetail().getKey());
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
