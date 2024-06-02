package com.hc.wx.mp.task;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SFTask implements Job {

    @Autowired
    private RedisTemplate redisTemplate;


    private static final String REDIS_LIST_KEY = "sf"; // 替换为你的Redis列表键
    private static final int BATCH_SIZE = 5;
    private static int offset = 0;
    private int count = 0;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String auth= (String) redisTemplate.opsForValue().get("auth");
        System.out.println("任务执行开始");
        Long listSize = redisTemplate.opsForList().size("Sf");
        List<String> items = redisTemplate.opsForList().range(REDIS_LIST_KEY, offset, offset + BATCH_SIZE - 1);
        if (items == null || items.isEmpty()) {
            System.out.println("当前集合为空");
            offset = 0;
            return;
        }
        processItems(items,auth);
        offset += BATCH_SIZE;
        count += 5;
        System.out.println("当前队列" + offset);
        if (count == listSize) {
            try {
                offset = 0;
                System.out.println("当前任务结束");
                jobExecutionContext.getScheduler().shutdown();
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processItems(List<String> items, String auth) {

        StringBuffer stringBuffer = new StringBuffer();
        String requestBody = "{\"name\":\"sfsyUrl\",\"value\":\"884024720\",\"remarks\":null,\"id\":1}";
        JSONObject jsonObject = JSONUtil.parseObj(requestBody);
        // 处理从Redis列表中读取的项目
        items.forEach(item -> {
            stringBuffer.append(item);
            stringBuffer.append("\n");
        });

        jsonObject.put("value", stringBuffer.toString());
        requestBody= jsonObject.toString();
        System.out.println(jsonObject);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://139.196.92.81:5700/api/envs?t=1717033389419")
                .put(RequestBody.create(requestBody.getBytes()))
                .header("Host", "139.196.92.81:5700")
                .header("Accept", "application/json, text/plain, */*")
                .header("Authorization", auth)
                .header("Accept-Language", "zh-CN,zh-Hans;q=0.9")
                .header("Content-Type", "application/json")
                .header("Origin", "http://139.196.92.81:5700")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Safari/605.1.15")
                .header("Referer", "http://139.196.92.81:5700/env")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
