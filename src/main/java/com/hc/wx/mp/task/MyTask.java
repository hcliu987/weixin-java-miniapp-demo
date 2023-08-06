package com.hc.wx.mp.task;

import cn.hutool.http.HttpUtil;
import com.hc.wx.mp.config.LotteryProperties;
import com.hc.wx.mp.config.NoticeProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Component
@Slf4j
public class MyTask {

    @Autowired
    private NoticeProperties properties;
    @Autowired
    private LotteryProperties lotteryProperties;
    Task task = new Task();

    public void notice(int money, String[] lastSsq, int[] zjNumber, String uToken) {
        String url = "https://api.day.app/" + uToken + "/本期福利双色球号码" + Arrays.toString(lastSsq) + ":中奖号码:" + Arrays.toString(zjNumber) + ",中奖金额：" + money;
        String serverUrl = "https://sctapi.ftqq.com/SCT142384Tq64Jxx4Jde2xQDQjct36FD4Z.send";
        String pushdeerUrl = "https://api2.pushdeer.com/message/push?pushkey=PDU21229TiqefGW5MIxU3C69S5KhM5efHWhtKpCUP&text=" + "本期福利双色球号码" + Arrays.toString(lastSsq) + ":中奖号码：" + Arrays.toString(zjNumber) + " 中奖金额：" + money;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 设置请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("text", "中奖通知");
        String desp = "本期福利双色球号码" + Arrays.toString(lastSsq) + ":中奖号码:" + Arrays.toString(zjNumber) + ",中奖金额：" + money;
        params.put("desp", desp);
        HttpUtil.get(url);
        log.info("\n调用通知信息：[{}]", url);
        HttpUtil.get(pushdeerUrl);
        log.info("\n调用通知信息：[{}]", pushdeerUrl);
        String pushPlus = "http://www.pushplus.plus/send?token=00e469292f6e4bee87b718d578417329&title=" + "本期福利双色球号码" + "&content=" + Arrays.toString(lastSsq) + ":中奖号码:" + Arrays.toString(zjNumber) + ",中奖金额：" + money;

        HttpUtil.post(serverUrl, params);
        log.info("\n调用通知信息：[{}, {}]", serverUrl, params);
        HttpUtil.get(pushPlus);
        log.info("\n调用通知信息：[{}]", pushPlus);
    }

    @Scheduled(cron = "15 15 22 ? * 2,4,7")
    public void run() throws InterruptedException {
        List myNumbers = new ArrayList<String>();
        myNumbers.add("3,7,12,16,20,27@11");
        myNumbers.add("5,10,15,21,24,28@9");
        myNumbers.add("2,11,13,19,26,32@6");
//        System.out.println();
//        try {
//            taskRunner.run(myNumbers, "hc");
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
        //  taskRunner.run(myNumbers,lotteryProperties);
        task.check(myNumbers, lotteryProperties);
    }


    @Scheduled(cron = "15 15 9 ? * FRI")
    public void runNotice() {
        String url = "https://api.day.app/" + properties.getBrakId() + "/i茅台游戏开始了?icon=https://picnew3.photophoto.cn/20090527/guizhoumaotaibiaozhitupian-10975341_1.jpg";
        String serverUrl = "https://sctapi.ftqq.com/SCT142384Tq64Jxx4Jde2xQDQjct36FD4Z.send";
        String pushdeerUrl = "https://api2.pushdeer.com/message/push?pushkey=PDU21229TiqefGW5MIxU3C69S5KhM5efHWhtKpCUP&text=i茅台游戏开始了";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 设置请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("text", "游戏开始通知");
        String desp = "i茅台游戏开始了";
        params.put("desp", desp);
        HttpUtil.get(url);
        log.info("\n调用通知信息：[{}]", url);
        HttpUtil.get(pushdeerUrl);
        log.info("\n调用通知信息：[{}]", pushdeerUrl);
        String pushPlus = "http://www.pushplus.plus/send?token=00e469292f6e4bee87b718d578417329&title=i茅台游戏开始了";

        HttpUtil.post(serverUrl, params);
        log.info("\n调用通知信息：[{}, {}]", serverUrl, params);
        HttpUtil.get(pushPlus);
        log.info("\n调用通知信息：[{}]", pushPlus);
    }

    @Scheduled(cron = "15 15 9 ? * SAT")
    public void runNoticeWXK() {
        String url = "https://api.day.app/SbiEGyu2Y4DQcowSzGAV34/i茅台小茅运游戏开始?";
        HttpUtil.get(url);
        log.info("\n调用通知信息：[{}]", url);

    }

    @Scheduled(cron = "0 15 9 28,29 * ?")
    public void runNoticeMYSG() {
        String hcURL = "https://api.day.app/WXbF6u6v6iKfPyVD5ttCgK/i茅台小茅运申购?icon=https://picnew3.photophoto.cn/20090527/guizhoumaotaibiaozhitupian-10975341_1.jpg";
        String wkxURL = "https://api.day.app/SbiEGyu2Y4DQcowSzGAV34/i茅台小茅运申购";
        HttpUtil.get(hcURL);
        HttpUtil.get(wkxURL);
        log.info("\n调用通知信息：[{}]", hcURL);
        log.info("\n调用通知信息：[{}]", wkxURL);

    }


    // @Scheduled(cron = "0 8 8 * * ")
    public static void deleteAll() throws Exception {


    }

    public static void main(String[] args) throws Exception {
        String url = "https://api.day.app/WXbF6u6v6iKfPyVD5ttCgK/i茅台游戏开始了?icon=https://picnew3.photophoto.cn/20090527/guizhoumaotaibiaozhitupian-10975341_1.jpg";
        HttpUtil.get(url);
    }

}
