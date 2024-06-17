package com.hc.wx.mp.service;


import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hc.wx.mp.entity.JsonsRootBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

@Service
@Slf4j
public class SearchService {


    private static final String BASE_URL = "http://m.kkqws.com/v/api/";
    private static final int STRING_LENGTH_THRESHOLD = 40; // 避免硬编码
    private ExecutorService executorService = Executors.newFixedThreadPool(10); //

    public SearchService() {
    }

    // 将重复的请求逻辑抽象成一个私有方法
    private String sendRequest(String endpoint, String query) throws IOException {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        // 设置请求头
        setCommonRequestHeaders(httpConn);

        httpConn.setDoOutput(true);
        try (OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream())) {
            writer.write(query);
            writer.flush();
        }

        // 处理响应
        try (InputStream responseStream = getResponseStream(httpConn); Scanner s = new Scanner(responseStream).useDelimiter("\\A")) {
            return s.hasNext() ? s.next() : "";
        }
    }

    private void setCommonRequestHeaders(HttpURLConnection connection) {
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        connection.setRequestProperty("Accept-Language", "zh-CN,zh-Hans;q=0.9");
        connection.setRequestProperty("Host", "m.kkqws.com");
        connection.setRequestProperty("Origin", "http://m.kkqws.com");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Safari/605.1.15");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Referer", "http://m.kkqws.com/app/index.html");
    }

    private InputStream getResponseStream(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode / 100 == 2) {
            String contentEncoding = connection.getContentEncoding();
            if ("gzip".equals(contentEncoding)) {
                return new GZIPInputStream(connection.getInputStream());
            }
            return connection.getInputStream();
        } else {
            return connection.getErrorStream();
        }
    }

    public String search(String text) {
        String query = "name=%" + text + "&token=i69";
        try {
            return sendRequest("search", query);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getJuzi(String text) {
        String query = "name=%" + text + "%&token=i69";
        try {
            return sendRequest("getJuzi", query);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String getXiaoyu(String text) {
        String query = "name=%" + text + "&token=i69";
        try {
            return sendRequest("getXiaoyu", query);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public List<JsonsRootBean> result(String text) throws Exception {
        ArrayList<JsonsRootBean> objects = new ArrayList<>();
        try {
            String encode = URLEncoder.encode(text, "UTF-8");
            List<String> tasks = Arrays.asList(search(text), getJuzi(text), getXiaoyu(text), getJuzi(text));
            List<Future<JsonsRootBean>> futures = tasks.stream().map(task -> executorService.submit(() -> analysisJson(task))).collect(Collectors.toList());
            for (Future<JsonsRootBean> future : futures) {
                try {
                    JsonsRootBean jsonsRootBean = future.get(5, TimeUnit.SECONDS);
                    objects.add(jsonsRootBean);
                } catch (Exception e) {
                    log.error("处理任务时发生异常", e);
                }
            }
        } catch (Exception e) {
            log.error("主方法中发生异常", e);
        }


        return objects;
    }

    public JsonsRootBean analysisJson(String json) {
        JsonsRootBean jsonsRootBean = null;

        if (!StrUtil.hasEmpty(json) && JSONUtil.isTypeJSON(json)) {
            jsonsRootBean = JSONUtil.toBean(json, JsonsRootBean.class);
            return jsonsRootBean;

        }
        return jsonsRootBean;
    }

    public String resultMsg(String text) throws Exception {
        long startTime = System.currentTimeMillis();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        StringBuilder sb = new StringBuilder();
        //   List<Future<String>> futures;
//        List<Callable<String>> tasks = Arrays.asList(
//                () -> processText(text, search(text)),
//                () -> processText(text, getJuzi(text)),
//                () -> processText(text, getXiaoyu(text))
//        );
//        try {
//            futures = executorService.invokeAll(tasks, 5, TimeUnit.SECONDS);
//            for (Future<String> future : futures) {
//                try {
//                    sb.append(future.get());
//                } catch (Exception e) {
//                    log.error("处理任务时发生异常", e);
//                }
//            }
//        } catch (InterruptedException e) {
//            log.error("处理任务时被中断", e);
//        } finally {
//            executorService.shutdown();
//        }


        List<CompletableFuture<String>> futures = Arrays.asList(
                CompletableFuture.supplyAsync(() -> processText(search(text))),
                CompletableFuture.supplyAsync(() -> processText(getJuzi(text))),
                CompletableFuture.supplyAsync(() -> processText(getXiaoyu(text))));

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.join();
        futures.forEach(future -> {
            try {
                sb.append(future.get());
            } catch (Exception e) {
                log.error("处理任务时发生异常", e);
            }
        });


        //等待所有任务完成
        long endTime = System.currentTimeMillis();
        stopWatch.stop();
        System.out.printf("当前方法执行时长: %s 秒. %n", stopWatch.getTotalTimeSeconds() + "");
        log.info("当前方法查询时间: %d 秒", (endTime - startTime) / 1000);
        return sb.toString();
    }

    public String processText(String source) {
        StringBuffer sb = new StringBuffer();
        if (source != null && source.length() > STRING_LENGTH_THRESHOLD) {
            JsonsRootBean bean = analysisJson(source);
            if (bean != null && bean.getList() != null && !bean.getList().isEmpty()) {

                bean.getList().stream().forEach(list -> {
                     sb.append(list.getAnswer()).append("\n");
                });
            }
        }
        return sb.toString();
    }


    /**
     * 获取任意结果
     */
    public String resultMsgAny(String text) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("获取任务结果开始");
        String result = "";
//        List<Callable<String>> tasks = Arrays.asList(
//                () -> processText(text, search(text)),
//                () -> processText(text, getJuzi(text)),
//                () -> processText(text, getXiaoyu(text))
//        );
//        try {
//            result = executorService.invokeAny(tasks, 5, TimeUnit.SECONDS);
//        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//            log.error("An error occurred while executing tasks: " + e.getMessage());
//            return "Error occurred: " + e.getMessage();
//        } finally {
//            executorService.shutdown();
//        }
        List<CompletableFuture<String>> futures = Arrays.asList(
                CompletableFuture.supplyAsync(() -> processText(search(text))),
                CompletableFuture.supplyAsync(() -> processText(getJuzi(text))),
                CompletableFuture.supplyAsync(() -> processText(getXiaoyu(text))));


        result = futures.stream().map(CompletableFuture::join).filter(Objects::nonNull).findFirst().orElse("No result found");


        stopWatch.stop();
        log.info("获取任务结果结束，耗时：{}", stopWatch.getTotalTimeSeconds() + "秒");

        return result;
    }


}