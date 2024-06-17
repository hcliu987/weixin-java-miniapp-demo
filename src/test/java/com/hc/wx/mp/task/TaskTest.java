package com.hc.wx.mp.task;

import cn.hutool.core.io.file.FileReader;
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
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.io.File;
import java.util.List;

@SpringBootTest
class TaskTest {


    @Autowired
    Task task;
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void appointmentResults() throws InterruptedException, WxErrorException {
        FileReader fileReader = FileReader.create(new File("/Users/liuhaicheng/Desktop/1.txt"));
        String[] split = fileReader.readString().split("\n");
        redisTemplate.opsForList().rightPushAll("sf", split);

    }

    private static final String REDIS_LIST_KEY = "sf"; // 替换为你的Redis列表键
    private static final int BATCH_SIZE = 4;
    private static int offset = 0;

    @Test
    public void sfTask() {
        String auth = (String) redisTemplate.opsForValue().get("auth");
        StringBuffer stringBuffer = new StringBuffer();
        String requestBody = "{\"name\":\"sfsyUrl\",\"value\":\"884024720\",\"remarks\":null,\"id\":1}";
        JSONObject jsonObject = JSONUtil.parseObj(requestBody);
        List<String> items = redisTemplate.opsForList().range(REDIS_LIST_KEY, offset, Math.min(offset + BATCH_SIZE - 1, 42));

        // 处理从Redis列表中读取的项目
        items.forEach(item -> stringBuffer.append(item).append("\n"));

        jsonObject.put("value", stringBuffer.toString());
        requestBody = jsonObject.toString();


        HttpURLConnection httpConn = null;
        try {
            URL url = new URL("http://139.196.92.81:5700/api/envs?t=1718174565429");
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("PUT");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        httpConn.setRequestProperty("Host", "139.196.92.81:5700");
        httpConn.setRequestProperty("Accept", "application/json, text/plain, */*");
        httpConn.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiWjRWVXV2c1pTTWhYUzFXaXhTVkVXMDBwWm5fenhEYjJreHJDcmpWOEcteklkY1B5SHpYMVg2NnRWLTBvbWgxOVBieGhQVTBQMG1VZEUxTm1hMlNGS05TZiIsImlhdCI6MTcxODE3MjQ3NywiZXhwIjoxNzE5OTAwNDc3fQ.N6_E4tqjqAb6s4IzfxWW_9GpXxmX9467YVFVyKbe75nnj93o70oIeDWW_Yzh0Vb2");
        httpConn.setRequestProperty("Accept-Language", "zh-CN,zh-Hans;q=0.9");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Origin", "http://139.196.92.81:5700");
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Safari/605.1.15");
        httpConn.setRequestProperty("Referer", "http://139.196.92.81:5700/env");

        httpConn.setDoOutput(true);
        try {
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write(requestBody);
            writer.flush();
            writer.close();
            httpConn.getOutputStream().close();

            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                    ? httpConn.getInputStream()
                    : httpConn.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";
            System.out.println(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}