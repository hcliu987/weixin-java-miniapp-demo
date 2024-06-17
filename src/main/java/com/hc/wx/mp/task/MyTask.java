package com.hc.wx.mp.task;

import com.hc.wx.mp.config.LotteryProperties;
import com.hc.wx.mp.config.NoticeProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
@Slf4j
@EnableScheduling
public class MyTask {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private NoticeProperties properties;
    @Autowired
    private LotteryProperties lotteryProperties;


    Task task = new Task();

    @Scheduled(cron = " 1 1 9  * * ?")
    public void runSf() {

        task.sfExrpNotity(redisTemplate, properties);
    }

    @Scheduled(cron = " 45 53 21 ? * 2,4,7")
    public void run() throws InterruptedException {
        log.info("定时任务执行开始");
        List myNumbers = new ArrayList<String>();
        myNumbers.add("5,7,12,13,17,30@13");
        myNumbers.add("6,9,12,21,25,26@6");
        myNumbers.add("2,7,13,17,26,33@1");
        myNumbers.add("1,7,10,23,31,32@16");
        myNumbers.add("19,23,24,28,29,32@014");
        String expect = task.lastExpect(lotteryProperties);

        task.check(myNumbers, expect, lotteryProperties, properties);

    }


    /**       Set keys = redisTemplate.keys("wx:*");
     //        System.out.println(keys);
     //        if (keys.size() > 0) {
     //
     //            keys.stream().forEach(
     //                    s -> {
     //
     //                        String key = s.toString();
     //                        LUser user = (LUser) redisTemplate.opsForValue().get(key);
     ////                        user.setMyNumbers( user.getMyNumbers().stream().map(
     ////                                myNumber ->
     ////                                        myNumber.replace("-", "@")
     ////
     ////                        ).collect(Collectors.toList()));
     //                        // task.check(user, lotteryProperties, properties);
     //                    }
     //            );

     }      **/

}
