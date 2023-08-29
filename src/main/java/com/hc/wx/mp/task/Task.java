package com.hc.wx.mp.task;

import cn.hutool.core.date.DateUnit;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hc.wx.mp.config.LotteryProperties;
import com.hc.wx.mp.config.RedisCache;
import com.hc.wx.mp.entity.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Component
public class Task {


    @Autowired
    RedisCache redisCache;

    public static void notice(ResultMsg bean) {
        String url = "https://api.day.app/WXbF6u6v6iKfPyVD5ttCgK/本期福利双色球号码:" + bean.getData().getOpenCode() + "购买号码:" + bean.getData().getCheckedCode() + ",中奖金额：" + bean.getData().getResultDetails() + "?icon=https://file2.rrxh5.cc/g2/c1/2018/09/20/1537439909849.png";
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
    public void check(List<String> myNumbers, LotteryProperties lotteryProperties) throws InterruptedException {
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


    }


    public void appointmentResults() {
        String version = getMTVersion();
        log.info("申购结果查询开始=========================");


        List<User> users = redisCache.getCacheList("user_list");



        for (User user : users) {
            try {
                String url = "https://app.moutai519.com.cn/xhr/front/mall/reservation/list/pageOne/query";
                String body = HttpUtil.createRequest(Method.GET, url)
                        .header("MT-Device-ID", user.getDeviceId())
                        .header("MT-APP-Version", version)
                        .header("MT-Token", user.getToken())
                        .header("User-Agent", "iOS;16.3;Apple;?unrecognized?").execute().body();
                JSONObject jsonObject = JSONObject.parseObject(body);
                System.out.println(jsonObject.toString());
                if (jsonObject.getInteger("code") != 2000) {
                    String message = jsonObject.getString("message");
                    throw new Exception(message);
                }
                for (Object itemVOs : jsonObject.getJSONObject("data").getJSONArray("reservationItemVOS")) {
                    JSONObject item = JSON.parseObject(itemVOs.toString());
                    // 预约时间在24小时内的
                    if (item.getInteger("status") == 2 && DateUtil.between(item.getDate("reservationTime"), new Date(), DateUnit.HOUR) < 24) {
                        String logContent = DateUtil.formatDate(item.getDate("reservationTime")) + " 申购" + item.getString("itemName") + "成功";
                        String urlBark = "https://api.day.app/WXbF6u6v6iKfPyVD5ttCgK/" + logContent;
                        HttpUtil.get(url);
                        log.info("\n调用通知信息：[{}]", url);
                    }

                }
            } catch (Exception e) {
                log.error("查询申购结果失败:失败原因{}", e.getMessage());
            }

        }
        log.info("申购结果查询结束=========================");
    }

    private static String getMTVersion() {
        String mtVersion = "";

        String url = "https://apps.apple.com/cn/app/i%E8%8C%85%E5%8F%B0/id1600482450";
        String htmlContent = HttpUtil.get(url);
        Pattern pattern = Pattern.compile("new__latest__version\">(.*?)</p>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(htmlContent);
        if (matcher.find()) {
            mtVersion = matcher.group(1);
            mtVersion = mtVersion.replace("版本 ", "");
        }

        return mtVersion;
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



