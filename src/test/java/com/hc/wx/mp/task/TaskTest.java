package com.hc.wx.mp.task;

import com.hc.wx.mp.config.LotteryProperties;
import com.hc.wx.mp.config.NoticeProperties;
import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
class TaskTest {


    @Autowired
    Task task;
    @Autowired

    private NoticeProperties properties;
    @Autowired
    private LotteryProperties lotteryProperties;
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void appointmentResults() throws InterruptedException, WxErrorException {
        String expect = task.lastExpect(lotteryProperties);
        Set keys = redisTemplate.keys("sf:*");
        System.out.println(keys);

    }
    @Test
    public  void test(){
        LocalDate now=LocalDate.now();
        System.out.println(now.toString().replaceAll("-",""));
    }


}