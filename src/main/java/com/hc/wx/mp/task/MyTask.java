package com.hc.wx.mp.task;

import cn.hutool.http.HttpUtil;
import com.hc.wx.mp.config.LotteryProperties;
import com.hc.wx.mp.config.NoticeProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class MyTask {
    @Autowired
    private NoticeProperties properties;
    @Autowired
    private LotteryProperties lotteryProperties;
    Task task = new Task();


    @Scheduled(cron = "15 45 21 ? * 2,4,7")
    public void run() throws InterruptedException {
        List myNumbers = new ArrayList<String>();
        myNumbers.add("3,7,12,16,20,27@11");
        myNumbers.add("5,10,15,21,24,28@9");
        myNumbers.add("2,11,13,19,26,32@6");
        myNumbers.add("1,13,15,29,30,31@12");
        myNumbers.add("4,9,10,11,23,27@5");
//        System.out.println();
//        try {
//            taskRunner.run(myNumbers, "hc");
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
        //  taskRunner.run(myNumbers,lotteryProperties);
        task.check(myNumbers, lotteryProperties,properties);
    }


    @Scheduled(cron = "0 5 18 ? * *")
    public  void appointmentResults(){
        task.appointmentResults();

    }
    public static void main(String[] args) throws InterruptedException {
        List myNumbers = new ArrayList<String>();
        myNumbers.add("3,7,12,16,20,27@11");
        myNumbers.add("5,10,15,21,24,28@9");
        myNumbers.add("2,11,13,19,26,32@6");
        myNumbers.add("1,13,15,29,30,31@12");
        myNumbers.add("4,9,10,11,23,27@5");
        Task task = new Task();
        LotteryProperties lotteryProperties = new LotteryProperties();
        NoticeProperties noticeProperties = new NoticeProperties();
        lotteryProperties.setAPPID("vinnsglluwk0brol");
        lotteryProperties.setAPPSECRET("HbKwaYgoIr1DhFFZ9rFoHEHhHZB1bYUT");
        noticeProperties.setBrakId("VVtPqTFKkTfBFLorEvLDX3");
        task.check(myNumbers, lotteryProperties,noticeProperties);
    }
}
