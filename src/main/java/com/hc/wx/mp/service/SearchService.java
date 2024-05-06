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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

@Service
@Slf4j
public class SearchService {

    public String getJuzi(String text) throws IOException {
        URL url = new URL("http://uukk6.cn/v/api/getJuzi");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpConn.setRequestProperty("Accept", "*/*");
        httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        httpConn.setRequestProperty("Accept-Language", "zh-CN,zh-Hans;q=0.9");
        httpConn.setRequestProperty("Host", "uukk6.cn");
        httpConn.setRequestProperty("Origin", "http://uukk6.cn");
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Safari/605.1.15");
        httpConn.setRequestProperty("Connection", "keep-alive");
        httpConn.setRequestProperty("Content-Length", "51");
        httpConn.setRequestProperty("Cookie", "Hm_lpvt_cce07f87930c14786d9eced9c08d0e89=1710469375; Hm_lvt_cce07f87930c14786d9eced9c08d0e89=1710422805");
        httpConn.setRequestProperty("Proxy-Connection", "keep-alive");
        httpConn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write("name=%" + text + "%&token=i69");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        if ("gzip".equals(httpConn.getContentEncoding())) {
            responseStream = new GZIPInputStream(responseStream);
        }
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        return response;
    }

    public String getDyfx(String text) throws Exception {
        URL url = new URL("http://uukk6.cn/v/api/getDyfx");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpConn.setRequestProperty("Accept", "*/*");
        httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        httpConn.setRequestProperty("Accept-Language", "zh-CN,zh-Hans;q=0.9");
        httpConn.setRequestProperty("Host", "uukk6.cn");
        httpConn.setRequestProperty("Origin", "http://uukk6.cn");
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Safari/605.1.15");
        httpConn.setRequestProperty("Connection", "keep-alive");
        httpConn.setRequestProperty("Content-Length", "51");
        httpConn.setRequestProperty("Cookie", "Hm_lpvt_cce07f87930c14786d9eced9c08d0e89=1710469375; Hm_lvt_cce07f87930c14786d9eced9c08d0e89=1710422805");
        httpConn.setRequestProperty("Proxy-Connection", "keep-alive");
        httpConn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write("name=%" + text + "%&token=i69");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        if ("gzip".equals(httpConn.getContentEncoding())) {
            responseStream = new GZIPInputStream(responseStream);
        }
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        return response;
    }

    public String getJuzi2(String text) throws Exception {
        URL url = new URL("http://m.kkqws.com/v/api/getJuzi");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpConn.setRequestProperty("Accept", "*/*");
        httpConn.setRequestProperty("Accept-Language", "zh-CN,zh-Hans;q=0.9");
        httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        httpConn.setRequestProperty("Host", "m.kkqws.com");
        httpConn.setRequestProperty("Origin", "http://m.kkqws.com");
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Safari/605.1.15");
        httpConn.setRequestProperty("Connection", "keep-alive");
        httpConn.setRequestProperty("Referer", "http://m.kkqws.com/app/index.html?name=%E6%B1%9F%E6%B2%B3%E6%97%A5%E4%B8%8A&token=i69");
        httpConn.setRequestProperty("Content-Length", "51");
        httpConn.setRequestProperty("Cookie", "Hm_lpvt_83af2d74162d7cbbebdab5495e78e543=1710471278; Hm_lvt_83af2d74162d7cbbebdab5495e78e543=1710466479");
        httpConn.setRequestProperty("Proxy-Connection", "keep-alive");
        httpConn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write("name=%" + text + "&token=i69");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        if ("gzip".equals(httpConn.getContentEncoding())) {
            responseStream = new GZIPInputStream(responseStream);
        }
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        return response;

    }

    public String getXiaoyu(String text) throws Exception {
        URL url = new URL("http://m.kkqws.com/v/api/getXiaoyu");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpConn.setRequestProperty("Accept", "*/*");
        httpConn.setRequestProperty("Accept-Language", "zh-CN,zh-Hans;q=0.9");
        httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        httpConn.setRequestProperty("Host", "m.kkqws.com");
        httpConn.setRequestProperty("Origin", "http://m.kkqws.com");
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Safari/605.1.15");
        httpConn.setRequestProperty("Connection", "keep-alive");
        httpConn.setRequestProperty("Referer", "http://m.kkqws.com/app/index.html?name=%E6%B1%9F%E6%B2%B3%E6%97%A5%E4%B8%8A&token=i69");
        httpConn.setRequestProperty("Content-Length", "51");
        httpConn.setRequestProperty("Cookie", "Hm_lpvt_83af2d74162d7cbbebdab5495e78e543=1710471278; Hm_lvt_83af2d74162d7cbbebdab5495e78e543=1710466479");
        httpConn.setRequestProperty("Proxy-Connection", "keep-alive");
        httpConn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write("name=%" + text + "&token=i69");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        if ("gzip".equals(httpConn.getContentEncoding())) {
            responseStream = new GZIPInputStream(responseStream);
        }
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        return response;
    }

    public String search(String text) throws Exception {
        URL url = new URL("http://m.kkqws.com/v/api/search");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpConn.setRequestProperty("Accept", "*/*");
        httpConn.setRequestProperty("Accept-Language", "zh-CN,zh-Hans;q=0.9");
        httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        httpConn.setRequestProperty("Host", "m.kkqws.com");
        httpConn.setRequestProperty("Origin", "http://m.kkqws.com");
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Safari/605.1.15");
        httpConn.setRequestProperty("Connection", "keep-alive");
        httpConn.setRequestProperty("Referer", "http://m.kkqws.com/app/index.html?name=%E6%B1%9F%E6%B2%B3%E6%97%A5%E4%B8%8A&token=i69");
        httpConn.setRequestProperty("Content-Length", "51");
        httpConn.setRequestProperty("Cookie", "Hm_lpvt_83af2d74162d7cbbebdab5495e78e543=1710471278; Hm_lvt_83af2d74162d7cbbebdab5495e78e543=1710466479");
        httpConn.setRequestProperty("Proxy-Connection", "keep-alive");
        httpConn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write("name=%" + text + "&token=i69");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        if ("gzip".equals(httpConn.getContentEncoding())) {
            responseStream = new GZIPInputStream(responseStream);
        }
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        return response;
    }

    public List<JsonsRootBean> result(String text) throws Exception {
        String encode = URLEncoder.encode(text, "UTF-8");
        ArrayList<JsonsRootBean> objects = new ArrayList<>();
        System.out.println();
        objects.add(analysisJson(search(text)));
        objects.add(analysisJson(getDyfx(text)));
        objects.add(analysisJson(getJuzi(text)));
        objects.add(analysisJson(getXiaoyu(text)));
        objects.add(analysisJson(getJuzi2(text)));
        return objects;
    }

    public JsonsRootBean analysisJson(String json) {
        JsonsRootBean jsonsRootBean = null;

        if (!StrUtil.hasEmpty(json)) {
            if (JSONUtil.isTypeJSON(json)) {
                jsonsRootBean = JSONUtil.toBean(json, JsonsRootBean.class);
                return jsonsRootBean;
            }

        }
        return jsonsRootBean;
    }

    public String resultMsg(String text) throws Exception {
        StringBuilder sb = new StringBuilder();
        long startTime = System.currentTimeMillis();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                if (search(text).length() > 40) {
                    if (analysisJson(search(text)).getList() != null) {
                        if (analysisJson(search(text)).getList().size() > 0) {

                            sb.append(analysisJson(search(text)).getList().get(0).getAnswer());
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return sb.toString();
        },executorService);
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                if (getDyfx(text).length() > 40) {
                    if (getDyfx(text) != null) {
                        if (analysisJson(getDyfx(text)).getList().size() > 0) {
                            sb.append(analysisJson(getDyfx(text)).getList().get(0).getAnswer());
                        }

                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return sb.toString();
        },executorService);
        CompletableFuture<String> stringCompletableFuture1 = CompletableFuture.supplyAsync(() -> {

            try {
                if (getJuzi(text).length() > 40) {
                    if (getJuzi(text) != null) {
                        if (analysisJson(getJuzi(text)).getList().size() > 0) {
                            sb.append(analysisJson(getJuzi(text)).getList().get(0).getAnswer());
                        }

                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return sb.toString();
        },executorService);

        CompletableFuture<String> stringCompletableFuture2 = CompletableFuture.supplyAsync(() -> {
            try {
                if (getJuzi(text).length() > 40) {
                    if (getXiaoyu(text) != null) {
                        if (analysisJson(getXiaoyu(text)).getList().size() > 0) {
                            sb.append(analysisJson(getXiaoyu(text)).getList().get(0).getAnswer());
                        }

                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return sb.toString();
        },executorService);

        CompletableFuture<Object> allOf = CompletableFuture.anyOf(future, stringCompletableFuture, stringCompletableFuture1,stringCompletableFuture2);
        System.out.println(allOf.get());


        long endTime = System.currentTimeMillis();
        stopWatch.stop();
        System.out.printf("当前方法查询时间: %d 秒. %n", (endTime - startTime) / 1000);
        System.out.printf("当前方法执行时长: %s 秒. %n", stopWatch.getTotalTimeSeconds() + "");
        log.info("当前方法查询时间: %d 秒", (endTime - startTime) / 1000);
        return (String) allOf.get();
    }
}