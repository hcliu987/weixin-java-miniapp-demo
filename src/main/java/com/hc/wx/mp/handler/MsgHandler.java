package com.hc.wx.mp.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.hc.wx.mp.builder.TextBuilder;
import com.hc.wx.mp.config.LotteryProperties;
import com.hc.wx.mp.config.RedisCache;
import com.hc.wx.mp.entity.JsonsRootBean;
import com.hc.wx.mp.entity.Lists;
import com.hc.wx.mp.task.Task;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import okhttp3.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@NoArgsConstructor
@Data
@Component
public class MsgHandler extends AbstractHandler {


    @Autowired
    private LotteryProperties lotteryProperties;
    @Autowired
    private RedisCache redisCache;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地
        }

        String content = wxMessage.getContent();
        if (wxMessage.getToUser().equals("gh_40edbd47dfdd")) {

            if (content.length() > 20 && content.contains("\n") && content.contains("@")) {
                //获取最新一期
                String lastExpect = Task.lastExpect(lotteryProperties);
                List myNumbers = Arrays.asList(content.split("\n"));

            }
        }
        String uToken = "";


        try {
            content = messageTest(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.info("当前用户{}查询的内容:{}", wxMessage.getFromUser(), wxMessage.getContent());
        logger.info("当前用户查询的结果:{}", content);
        return new TextBuilder().build(content, wxMessage, weixinService);

    }


    private static String messageTest(String text) throws Exception {


        String getKK = null;
        try {
            getKK = analysisJson(getKK(text));
            System.out.println(getKK);
            System.out.println("getKK = " + getKK);
            if (getKK.length() <= 8) {
                return "没查询到相关内容";
            }
        } catch (Exception e) {
            HttpUtil.get("https://api.day.app/VVtPqTFKkTfBFLorEvLDX3/自助影视资源查询发生异常" + e.getMessage());
        }
        if (getKK.length() > 8) {

            return getKK.length() < 600 ? getKK : getKK.substring(0, 300);
        }


        return "没查询到相关内容";
    }


    public static String getKK(String text) throws Exception {
        OkHttpClient client = new OkHttpClient();

        String token=getToken();
        String encode = URLEncoder.encode(text, "UTF-8");

        RequestBody formBody = new FormBody.Builder()
                .add("name", text)
                .add("token", token)
                .build();

        Request request = new Request.Builder()
                .url("http://m.9dups.com/v/api/getKK")
                .post(formBody)
                .header("Host", "m.9dups.com")
                .header("Accept", "*/*")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Accept-Language", "zh-CN,zh-Hans;q=0.9")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Origin", "http://m.9dups.com")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2.1 Safari/605.1.15")
                .header("Referer", "http://m.9dups.com/app/index.html?name=%"+encode+"&id=test&token="+token)
                .header("Cookie", "Hm_lpvt_0606cc863fe63b0953d06585b4897bcb=1705107327; Hm_lvt_0606cc863fe63b0953d06585b4897bcb=1704955344")
                .header("Proxy-Connection", "keep-alive")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }


    private static String getToken() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://m.9dups.com/v/api/getToken")
                .header("Host", "m.9dups.com")
                .header("Cookie", "Hm_lpvt_0606cc863fe63b0953d06585b4897bcb=1705107048; Hm_lvt_0606cc863fe63b0953d06585b4897bcb=1704955344")
                .header("If-None-Match", "W/\"27-D9bnsYCdPGR0e+M4DGjmIg9CSbs\"")
                .header("Accept", "*/*")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2.1 Safari/605.1.15")
                .header("Referer", "http://m.9dups.com/app/index.html?name=%E7%B9%81%E8%8A%B1&id=test&token=18im4napra8")
                .header("Accept-Language", "zh-CN,zh-Hans;q=0.9")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Proxy-Connection", "keep-alive")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return JSONUtil.parseObj(response.body().string()).getStr("token");
        }

    }


    public static String analysisJson(String json) {


        if (!StrUtil.hasEmpty(json)) {
            if (JSONUtil.isTypeJSON(json)) {
                JsonsRootBean jsonsRootBean = JSONUtil.toBean(json, JsonsRootBean.class);
                StringBuffer sb = new StringBuffer();
                for (Lists lists : jsonsRootBean.getList()) {

                    sb.append(lists + "\r\n");

                }
                return sb.toString();
            }

        }
        return json;
    }

    public static void main(String[] args) throws Exception {

        System.out.println(messageTest("繁花"));
    }

}
