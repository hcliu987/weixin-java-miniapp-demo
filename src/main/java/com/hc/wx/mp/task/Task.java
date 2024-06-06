package com.hc.wx.mp.task;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.hc.wx.mp.config.LotteryProperties;
import com.hc.wx.mp.config.NoticeProperties;
import com.hc.wx.mp.entity.SFUser;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Task {
    @Autowired
    WxMpService wxService;


    public void sfExrpNotity(RedisTemplate redisTemplate, NoticeProperties properties) {
        LocalDate now = LocalDate.now();
        Set<String> keys = redisTemplate.keys("sf:*");
        List<SFUser> sfUsers = new ArrayList<>();
        if (keys.isEmpty()) {
            keys.stream().forEach(v -> {
                sfUsers.add((SFUser) redisTemplate.opsForValue().get(v.toString()));
            });
        }
        sfUsers.stream().forEach(
                user -> {

                    if (user.getExpTime().equals(now.toString().replaceAll("-", ""))) {
                        String urlBark = "https://api.day.app/" + properties.getBrakId() + "/" + user.getPhone() + "/当前手机号过期";
                        HttpUtil.get(urlBark);
                    }
                }
        );
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

    public String getSFurl(){
        FileReader fileReader = new FileReader("/Users/liuhaicheng/Desktop/1.txt");
        String result = fileReader.readString();
        System.out.println(result);
        return ";";
    }

    public void sfCheck(RedisTemplate redisTemplate, NoticeProperties properties,int number) {
        String s = redisTemplate.opsForValue().get("sf", 0, number-1);
        System.out.println(s);
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

    class SFBean {
        private String phone;
        private String expDate;

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