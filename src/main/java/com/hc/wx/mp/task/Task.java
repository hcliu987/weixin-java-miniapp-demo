package com.hc.wx.mp.task;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hc.wx.mp.config.LotteryProperties;
import com.hc.wx.mp.config.NoticeProperties;
import com.hc.wx.mp.config.RedisCache;
import com.hc.wx.mp.entity.LUser;
import com.hc.wx.mp.entity.MtUser;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class Task {
    @Autowired
    WxMpService wxService;
    @Autowired
    private LotteryProperties lotteryProperties;

    @Autowired
    RedisCache redisCache;
    @Autowired
    public RedisTemplate redisTemplate;
    @Autowired
    private NoticeProperties properties;

    public void appointmentResults() {
        String version = getMTVersion();
        log.info("申购结果查询开始=========================");
        List<MtUser> users = new ArrayList<>();
        if (redisTemplate != null) {
            Set mt = redisTemplate.keys("mt*");
            mt.stream().forEach(s -> {
                users.add(redisCache.getCacheObject(s.toString()));

            });
        }

        users.stream().forEach(user -> {
            try {
                String url = "https://app.moutai519.com.cn/xhr/front/mall/reservation/list/pageOne/query";
                String body = HttpUtil.createRequest(Method.GET, url)
                        .header("MT-Device-ID", user.getDeviceId())
                        .header("MT-APP-Version", version)
                        .header("MT-Token", user.getToken())
                        .header("User-Agent", "iOS;16.3;Apple;?unrecognized?").execute().body();
                JSONObject jsonObject = JSONObject.parseObject(body);
                if (jsonObject.getInteger("code") != 2000) {
                    String message = jsonObject.getString("message");
                    throw new Exception(message);
                }
                for (Object itemVOs : jsonObject.getJSONObject("data").getJSONArray("reservationItemVOS")) {
                    JSONObject item = JSON.parseObject(itemVOs.toString());
                    // 预约时间在24小时内的
                    if (item.getInteger("status") == 2 && DateUtil.between(item.getDate("reservationTime"), new Date(), DateUnit.HOUR) < 24) {
                        String logContent = DateUtil.formatDate(item.getDate("reservationTime")) + " 申购" + item.getString("itemName") + "成功";
                        String urlBark = "https://api.day.app/" + properties.getBrakId() + "/" + logContent + "/申购者：" + user.getName();
                        HttpUtil.get(url);
                        log.info("\n调用通知信息：[{}]", url);
                    }

                }
            } catch (Exception e) {
                log.error("查询申购结果失败:失败原因{}", e.getMessage());
            }

        });

    }

    private String getMTVersion() {
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
     * 根据购买单号查询中奖资格
     */
    public void check(List<String> myNumbers, String expect, LotteryProperties lotteryProperties, NoticeProperties properties) throws InterruptedException {
        log.info("任务开始");
        log.info(expect);
        if (!myNumbers.isEmpty()) {
            log.info(myNumbers.size() + "");
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
                System.out.println(bean.toString());
                properties.setBrakId("VVtPqTFKkTfBFLorEvLDX3");
                if (!bean.getData().resultDetails.contains("暂未中奖")) {
                    notice(bean, properties.getBrakId());
                }


            }
        }
    }

    private static void notice(ResultMsg bean, String brakId) {
        String url = "https://api.day.app/" + brakId + "/本期福利双色球号码:" + bean.getData().getOpenCode() + "购买号码:" + bean.getData().getCheckedCode() + ",中奖金额：" + bean.getData().getResultDetails() + "?icon=https://file2.rrxh5.cc/g2/c1/2018/09/20/1537439909849.png";
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
     * 获取最新一期彩票号
     */
    public String lastExpect(LotteryProperties lotteryProperties) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("code", "ssq");
        paramMap.put("size", "1");
        paramMap.put("app_id", lotteryProperties.getAPPID());
        paramMap.put("app_secret", lotteryProperties.getAPPSECRET());
        String result = HttpUtil.get(" https://www.mxnzp.com/api/lottery/common/history", paramMap);

        Result bean = JSONUtil.toBean(result, Result.class);

        return bean.getData().get(0).getExpect();

    }

    public void check(LUser user, LotteryProperties lotteryProperties, NoticeProperties properties) {
        if (user != null) {
            user.getMyNumbers().stream().forEach(
                    myNumbers -> {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Map<String, Object> paramMap = new HashMap<>();
                        paramMap.put("code", "ssq");
                        paramMap.put("expect", user.getLast());
                        paramMap.put("lotteryNo", myNumbers
                        );
                        paramMap.put("app_id", lotteryProperties.getAPPID());
                        paramMap.put("app_secret", lotteryProperties.getAPPSECRET());
                        String result = HttpUtil.get("https://www.mxnzp.com/api/lottery/common/check", paramMap);
                        System.out.println(result);
                        ResultMsg bean = JSONUtil.toBean(result, ResultMsg.class);

                        if (!bean.getData().resultDetails.contains("暂未中奖")) {
                            Map<String, Object> param = new HashMap<>();
                            param.put("touser", user.getId());
                            param.put("template_id", "pnNH0Sm2BZJc_Fj1ndbE9lKb6oBNMPUt0kVmcCA5GXA");
                            param.put("url", "");
                            Map<String, WeChatTemplateMsg> moeny = new HashMap<>();

                            System.out.println(bean.getData().getResultDetails().toString());
                            moeny.put("result", new WeChatTemplateMsg(bean.getData().getResultDetails().toString()));
                            moeny.put("last", new WeChatTemplateMsg(bean.getData().getExpect()));
                            moeny.put("list", new WeChatTemplateMsg(bean.getData().getCheckedCode()));
                            param.put("data", moeny);
                            String post = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx6a36791feb98c796&secret=59538ba157b64d663001e273740c3ccb");
                            String string = JSONUtil.parseObj(param).toString();
                            String resultChat = HttpUtil.post("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + JSONUtil.parseObj(post).getStr("access_token"), string);
                        }
                    }
            );
        }
    }


    @Data
    @ToString
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


    @Data
    class WeChatTemplateMsg {
        /**
         * 消息
         */
        private String value;
        /**
         * 消息颜色
         */
        private String color;


        public WeChatTemplateMsg(String value) {
            this.value = value;
            this.color = "#173177";
        }

        public WeChatTemplateMsg(String value, String color) {
            this.value = value;
            this.color = color;
        }

    }

}