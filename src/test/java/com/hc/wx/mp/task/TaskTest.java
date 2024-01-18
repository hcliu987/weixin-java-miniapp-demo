package com.hc.wx.mp.task;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.hc.wx.mp.config.LotteryProperties;
import com.hc.wx.mp.config.NoticeProperties;
import com.hc.wx.mp.config.WxMpProperties;
import com.hc.wx.mp.entity.LUser;
import lombok.Data;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
class TaskTest {


    @Autowired
    Task task;
    @Autowired

    private NoticeProperties properties;
    @Autowired
    private LotteryProperties lotteryProperties;
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void appointmentResults() throws InterruptedException, WxErrorException {
        String expect = task.lastExpect(lotteryProperties);
        Set keys = redisTemplate.keys("wx:*");
        System.out.println(keys);
        if (keys.size()> 0) {

            keys.stream().forEach(
                    s -> {

                        String key = s.toString();
                        LUser user = (LUser) redisTemplate.opsForValue().get(key);
                        task.check(user, lotteryProperties, properties);
                    }
            );

        }

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