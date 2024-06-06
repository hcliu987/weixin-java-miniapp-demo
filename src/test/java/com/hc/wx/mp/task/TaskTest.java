package com.hc.wx.mp.task;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class TaskTest {


    @Autowired
    Task task;
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void appointmentResults() throws InterruptedException, WxErrorException {
        //  FileReader fileReader = FileReader.create(new File("/Users/liuhaicheng/Desktop/1.txt"));
        //String[] split = fileReader.readString().split("\n");
        // redisTemplate.opsForList().rightPushAll("sf",split);

    }

    @Test
    public void test() {
//        String requestBody = "{\"name\":\"sfsyUrl\",\"value\":\"884024720\",\"remarks\":null,\"id\":1}";
//        JSONObject jsonObject = JSONUtil.parseObj(requestBody);
//        jsonObject.put("value","2");
//
//        System.out.println(jsonObject.toString());
        //  String auth ="Bearer eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiLWY5M0xtZ0M3X0RfcTFCLXZKNmNaMDM2MXFNd3YwazdPcU9JMWo2SmhPdlpzX2gzeloiLCJpYXQiOjE3MTcwNTU1NTksImV4cCI6MTcxODc4MzU1OX0.wIjJu9j2ilYGcFj8sSFrCCeCApli6-tI5vpzTXQkK53eWKoA256Bi0iJWKlrAq1t";
        // redisTemplate.opsForValue().set("auth",auth);
    }


}