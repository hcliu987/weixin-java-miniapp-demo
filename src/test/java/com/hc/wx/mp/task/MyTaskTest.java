package com.hc.wx.mp.task;

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
    @Test
    public void test() {

        Set keys = redisTemplate.keys("wx:*");
        System.out.println(keys);
        keys.stream().forEach(
                s -> {
                    String key = s.toString();
                    LUser user = (LUser) redisTemplate.opsForValue().get(key);
                    System.out.println(key);
                    List<String> newList = user.getMyNumbers().stream().map(
                            myNumber ->
                                    myNumber.replace("-", "@")
                    ).collect(Collectors.toList());
                    user.setMyNumbers(newList);
                    System.out.println(user.getMyNumbers().get(0));

                }
        );
//
    }
}
