package com.hc.wx.mp.task;

import cn.hutool.core.io.file.FileReader;
import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.jupiter.api.Test;
import org.quartz.DateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;import java.util.List;

@SpringBootTest
class TaskTest {


    @Autowired
    SFTask task;
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void appointmentResults() throws InterruptedException, WxErrorException {
        FileReader fileReader = FileReader.create(new File("/Users/liuhaicheng/Desktop/1.txt"));
        String[] split = fileReader.readString().split("\n");
        redisTemplate.opsForList().rightPushAll("sf", split);

    }

    @Test
    public void test() {

        redisTemplate.opsForValue().set("auth", "Bearer eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoidEJLLVhXUUFGVXdLSUxXN1NDTmt4N0lmbFk0bFgxTEhDLXhCRFk0TC1sa1FMbTN0OWRNVExGT09nMThxTmRudk1YN0NxSUNVOERyY0lvVkhJUHJ0djM5RUNDYXNBRHVYbHk1VSIsImlhdCI6MTczNTAwNjg5NCwiZXhwIjoxNzM2NzM0ODk0fQ.vD187Ajc3OlCMIRonL7CovGd_LN55vAt2rV53sylEN78UcLbg-X5ed9AzOr-lMTY");
    }

    @Test
    public void testInsert() {
        String REDIS_LIST_KEY = "sf";
        int offset = 0;
        int BATCH_SIZE = 5;
        Long listSize = redisTemplate.opsForList().size(REDIS_LIST_KEY);
        String auth = (String) redisTemplate.opsForValue().get("auth");
        List<String> items = redisTemplate.opsForList().range(REDIS_LIST_KEY, offset, Math.min(offset + BATCH_SIZE - 1, listSize));
        task.processItems(items,auth);
    }

    @Test
    public  void date(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = DateBuilder.todayAt(14,30,0);
        System.out.println(sdf.format(date));
    }


}