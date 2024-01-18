package com.hc.wx.mp.task;

import com.hc.wx.mp.config.LotteryProperties;
import com.hc.wx.mp.config.NoticeProperties;
import com.hc.wx.mp.config.RedisCache;
import com.hc.wx.mp.entity.LUser;
import com.hc.wx.mp.handler.MsgHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Component
@Slf4j
public class MyTask {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private NoticeProperties properties;
    @Autowired
    private LotteryProperties lotteryProperties;
    Task task = new Task();


    @Scheduled(cron = "45 50 21 ? * 2,4,7")
    public void run() throws InterruptedException {
        log.info("定时任务执行开始");
        List myNumbers = new ArrayList<String>();
        myNumbers.add("3,7,12,16,20,27@11");
        myNumbers.add("5,10,15,21,24,28@9");
        myNumbers.add("2,11,13,19,26,32@6");
        myNumbers.add("1,13,15,29,30,31@12");
        myNumbers.add("4,9,10,11,23,27@05");
        String expect = task.lastExpect(lotteryProperties);
        Set keys = redisTemplate.keys("wx:*");
        System.out.println(keys);
        if (keys.size()> 0) {

            keys.stream().forEach(
                    s -> {

                        String key = s.toString();
                        LUser  user = (LUser) redisTemplate.opsForValue().get(key);
//                        user.setMyNumbers( user.getMyNumbers().stream().map(
//                                myNumber ->
//                                        myNumber.replace("-", "@")
//
//                        ).collect(Collectors.toList()));
                        task.check(user, lotteryProperties, properties);
                    }
            );

        }
        task.check(myNumbers, expect, lotteryProperties, properties);
    }


    @Scheduled(cron = "0 5 18 ? * *")
    public void appointmentResults() {
        task.appointmentResults();

    }

    public static void main(String[] args) throws InterruptedException {

    }
}
