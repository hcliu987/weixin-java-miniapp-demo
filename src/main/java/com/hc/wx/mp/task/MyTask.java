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
//        log.info("定时任务执行开始");
//        List myNumbers = new ArrayList<String>();
//        myNumbers.add("2,13,17,18,21,26@13");
//        myNumbers.add("2,3,9,14,20,26@9");
//        myNumbers.add("2,13,16,25,28,33@8");
//        myNumbers.add("3,4,18,20,22,30@7");
//        myNumbers.add("4,14,18,24,27,29@011");
//        String expect = task.lastExpect(lotteryProperties);
//
//        task.check(myNumbers, expect, lotteryProperties, properties);

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
