package com.hc.wx.mp.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyTask {
    ArrayList myNumbers = new ArrayList<int[]>();

    TaskRunner taskRunner=new TaskRunner();
    @Scheduled(cron = "15 45 21 ? * 2,4,7")
    public void run(){
        myNumbers.add(new int[]{3, 15, 16, 21, 26, 32, 5});
        myNumbers.add(new int[]{1, 14, 16, 22, 26, 31, 12});
        myNumbers.add(new int[]{4, 13, 17, 27, 29, 30, 6});
        myNumbers.add(new int[]{4, 9, 11, 13, 27, 28, 11});
        myNumbers.add(new int[]{7, 11, 20, 23, 25, 32, 1});
        System.out.println();
        try {
            taskRunner.run(myNumbers);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
