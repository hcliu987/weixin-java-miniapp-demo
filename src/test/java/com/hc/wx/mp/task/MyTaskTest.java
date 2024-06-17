package com.hc.wx.mp.task;

import com.hc.wx.mp.config.LotteryProperties;
import com.hc.wx.mp.entity.LUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
public class MyTaskTest {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    Task task;
    @Autowired
    LotteryProperties lotteryProperties;
    @Test
    public void test() {

    }
}
