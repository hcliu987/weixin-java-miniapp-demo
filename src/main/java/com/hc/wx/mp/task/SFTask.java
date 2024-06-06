package com.hc.wx.mp.task;

import cn.hutool.core.date.StopWatch;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
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
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SFTask implements Job {

    @Autowired
    private RedisTemplate redisTemplate;


    private static final String REDIS_LIST_KEY = "sf"; // 替换为你的Redis列表键
    private static final int BATCH_SIZE = 5;
    private static int offset = 0;
    private static int count = 0;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        StopWatch stopWatch = new StopWatch();
        log.info("任务执行开始");
        stopWatch.start();
        String auth = (String) redisTemplate.opsForValue().get("auth");
        Long listSize = redisTemplate.opsForList().size("sf");
        log.info("当前拥有账号"+listSize);
        List<String> items = redisTemplate.opsForList().range(REDIS_LIST_KEY, offset, Math.min(offset + BATCH_SIZE - 1, listSize));

        if (items == null || items.isEmpty()) {
            log.info("当前集合为空");
            offset = 0;
            return;
        }
        processItems(items, auth);
        offset += BATCH_SIZE;
        log.info("当前查询记录: {}", offset);

        if (offset>=listSize) {
            try {
                offset = 0;
                log.info("当前任务结束");
                jobExecutionContext.getScheduler().shutdown();
            } catch (SchedulerException e) {
                log.error("调度器关闭时发生异常: {}", e.getMessage(), e);
                throw new RuntimeException("调度器关闭失败: " + e.getMessage(), e);
            }
        }
        stopWatch.stop();
        log.info("当前任务执行时间: {}", stopWatch.getTotalTimeSeconds());

    }

    private void processItems(List<String> items, String auth) {

        StringBuffer stringBuffer = new StringBuffer();
        String requestBody = "{\"name\":\"sfsyUrl\",\"value\":\"884024720\",\"remarks\":null,\"id\":1}";
        JSONObject jsonObject = JSONUtil.parseObj(requestBody);
        // 处理从Redis列表中读取的项目
        items.forEach(item -> stringBuffer.append(item).append("\n"));

        jsonObject.put("value", stringBuffer.toString());
        requestBody = jsonObject.toString();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        log.info("当前url"+requestBody);

//        Request request = new Request.Builder()
//                .url("http://139.196.92.81:5700/api/envs?t=1717033389419")
//                .put(RequestBody.create(requestBody.getBytes()))
//                .header("Host", "139.196.92.81:5700")
//                .header("Accept", "application/json, text/plain, */*")
//                .header("Authorization", auth)
//                .header("Accept-Language", "zh-CN,zh-Hans;q=0.9")
//                .header("Content-Type", "application/json")
//                .header("Origin", "http://139.196.92.81:5700")
//                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Safari/605.1.15")
//                .header("Referer", "http://139.196.92.81:5700/env")
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("Unexpected code " + response);
//            }
//            response.body().string();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
