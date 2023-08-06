package com.hc.wx.mp.task;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.hc.wx.mp.config.LotteryProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Slf4j
public class Task {
    public static void notice(ResultMsg bean) {
        String url = "https://api.day.app/WXbF6u6v6iKfPyVD5ttCgK/本期福利双色球号码:" + bean.getData().getOpenCode() + "购买号码:" + bean.getData().getCheckedCode() + ",中奖金额：" + bean.getData().getResultDetails()+"?icon=https://file2.rrxh5.cc/g2/c1/2018/09/20/1537439909849.png";
        String serverUrl = "https://sctapi.ftqq.com/SCT142384Tq64Jxx4Jde2xQDQjct36FD4Z.send";
        String pushdeerUrl = "https://api2.pushdeer.com/message/push?pushkey=PDU21229TiqefGW5MIxU3C69S5KhM5efHWhtKpCUP&text=" + "/本期福利双色球号码:" + bean.getData().getOpenCode() + "购买号码：" + bean.getData().getCheckedCode() + " 中奖金额：" + bean.getData().getResultDetails();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 设置请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("text", "中奖通知");
        String desp = "本期福利双色球号码:" + bean.getData().getOpenCode() + ":购买号码" + bean.getData().getCheckedCode() + ",中奖金额：" + bean.getData().getResultDetails();
        params.put("desp", desp);
        HttpUtil.get(url);
        log.info("\n调用通知信息：[{}]", url);
        HttpUtil.get(pushdeerUrl);
        log.info("\n调用通知信息：[{}]", pushdeerUrl);
        String pushPlus = "http://www.pushplus.plus/send?token=00e469292f6e4bee87b718d578417329&title=" + "/本期福利双色球号码:" + "&content=" + bean.getData().getOpenCode() + "购买号码:" + bean.getData().getCheckedCode() + ",中奖金额：" + bean.getData().getResultDetails();

        HttpUtil.post(serverUrl, params);
        log.info("\n调用通知信息：[{}, {}]", serverUrl, params);
        HttpUtil.get(pushPlus);
        log.info("\n调用通知信息：[{}]", pushPlus);
    }

    /**
     * 根据购买单号查询中奖资格
     */
    public  void check(List<String> myNumbers, LotteryProperties lotteryProperties) throws InterruptedException {
        String expect = lastExpect(lotteryProperties);
        if (!myNumbers.isEmpty()) {
            for (int i = 0; i < myNumbers.size(); i++) {
                TimeUnit.SECONDS.sleep(1);
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("code", "ssq");
                paramMap.put("expect", expect);
                paramMap.put("lotteryNo", myNumbers.get(i));
                paramMap.put("app_id", lotteryProperties.getAPPID());
                paramMap.put("app_secret", lotteryProperties.getAPPSECRET());
                String result = HttpUtil.get("https://www.mxnzp.com/api/lottery/common/check", paramMap);
                ResultMsg bean = JSONUtil.toBean(result, ResultMsg.class);
                notice(bean);

            }
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {
        List myNumbers = new ArrayList<String>();
        myNumbers.add("3,7,12,16,20,27@11");
        myNumbers.add("5,10,15,21,24,28@9");
        myNumbers.add("2,11,13,19,26,32@6");

        TaskRunner task = new TaskRunner();
        LotteryProperties lotteryProperties = new LotteryProperties();
        lotteryProperties.setAPPID("vinnsglluwk0brol");
        lotteryProperties.setAPPSECRET("HbKwaYgoIr1DhFFZ9rFoHEHhHZB1bYUT");

       // check(myNumbers, lotteryProperties);
    }

    /**
     * 获取最新一期彩票号
     */
    public static String lastExpect(LotteryProperties lotteryProperties) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("code", "ssq");
        paramMap.put("size", "1");
        paramMap.put("app_id", lotteryProperties.getAPPID());
        paramMap.put("app_secret", lotteryProperties.getAPPSECRET());
        String result = HttpUtil.get(" https://www.mxnzp.com/api/lottery/common/history", paramMap);

        Result bean = JSONUtil.toBean(result, Result.class);

        return bean.getData().get(0).getExpect();
    }

    @Data
    class ResultMsg {
        private Integer code;
        private String msg;
        private ResultMsgData data;
    }

    @Data
    class ResultMsgData {
        private List<ResultList> datas;
        private String resultDetails;
        private String resultDesc;
        private String openCode;
        private String checkedCode;
        private String expect;
        private String code;
        private String codeValue;
    }

    class ResultList {
        private Integer code;
        private Boolean lottery;
        private String blue;


    }

    @Data
    class Result {
        private Integer code;
        private String meg;
        private List<ResultData> data;
    }

    @Data
    class ResultData {
        private String openCode;
        private String code;
        private String expect;
        private String name;
        private String time;
    }


}



