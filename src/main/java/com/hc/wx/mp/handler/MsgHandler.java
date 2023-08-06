package com.hc.wx.mp.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hc.wx.mp.builder.TextBuilder;
import com.hc.wx.mp.entity.JsonsRootBean;
import com.hc.wx.mp.entity.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@NoArgsConstructor
@Data
@Component
public class MsgHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地
        }

        String uToken = "";
        //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
        try {
            if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
                    && weixinService.getKefuService().kfOnlineList()
                    .getKfOnlineList().size() > 0) {
                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
                        .fromUser(wxMessage.getToUser())
                        .toUser(wxMessage.getFromUser()).build();
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        String content = "";


        try {
            content = messageTest(wxMessage.getContent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (content.length() < 1) {
            content = "没查询到相关内容";
        }
        logger.info("当前用户{}查询的内容:{}", wxMessage.getFromUser(),wxMessage.getContent());
        logger.info("当前用户查询的结果:{}", content);
        return new TextBuilder().build(content, wxMessage, weixinService);

    }


    private static String messageTest(String text) throws Exception {


        String getTTZJB = null;
        String getJuzi = null;
        String sortWeb = null;
        try {
            getTTZJB = analysisJson(getTTZJB(text));
            getJuzi = analysisJson(getJuzi(text));
            sortWeb = analysisJson(sortWeb(text));
            System.out.println("sortWeb = " + sortWeb);
            if(getTTZJB.length()<=8||getJuzi.length()<=8||sortWeb.length()<=8){
                return "没查询到相关内容";
            }
        } catch (Exception e) {
            HttpUtil.get("https://api.day.app/VizApLTywWLsn4eUjHADRC/自助影视资源查询发生异常" + e.getMessage());
        }
        if (sortWeb.length() > 1) {

            return sortWeb.length()<600?sortWeb:sortWeb.substring(0,600);
        }
        if (getTTZJB.length() > getJuzi.length()) {
            return getTTZJB.length()<600?getTTZJB:getTTZJB.substring(0,600);
        }
        if (getJuzi.length() > getTTZJB.length()) {
            return getJuzi.length()<600?getJuzi:getJuzi.substring(0,600);
        }

        return "没查询到相关内容";
    }

    private static String sortWeb(String text) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String token = getToken();
        RequestBody formBody = new FormBody.Builder()
                .add("name", text)
                .add("token", token)
                .add("tabN", "movie_211229kl")
                .add("topNo", "10")
                .add("whr", "question like \"%" + text + "%\" or byname like \"%" + text + "%\"")
                .add("orderBy", "isTop DESC, date_time")
                .add("orderType", "DESC")
                .add("keys", "question,answer,isTop,id")
                .build();

        Request request = new Request.Builder()
                .url("http://ej666.com/v/api/sortWeb")
                .post(formBody)
                .header("Host", "ej666.com")
                .header("Accept", "*/*")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Accept-Language", "zh-CN,zh-Hans;q=0.9")
               // .header("Accept-Encoding", "gzip, deflate")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Origin", "http://ej666.com")
                .header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 16_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.5 Mobile/15E148 Safari/604.1")
                .header("Connection", "keep-alive")
                .header("Referer", "http://ej666.com/app/index.html?name=" + URLEncoder.encode(text, "UTF-8").toString() + "&id=221010nn&token=" + token + "&hideb=true&hidea=true")
                .header("Content-Length", "248")
                .header("Cookie", "Hm_lpvt_0606cc863fe63b0953d06585b4897bcb=1684757006; Hm_lvt_0606cc863fe63b0953d06585b4897bcb=1684756990")
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }

    }

    private static String getTTZJB(String text) {
        HashMap<String, String> headMap = new HashMap<>();
        headMap.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        headMap.put("Accept", "*/*");
        headMap.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148");
        headMap.put("Accept-Language", "zh-CN,zh-Hans;q=0.9");
        headMap.put("Accept-Encoding", "gzip, deflate");
        headMap.put("Connection", "keep-alive");
        // headMap.put("Host", "175.178.114.175:8090");
        HashMap<String, Object> bodyMap = new HashMap<>();
        JSONObject json = new JSONObject();
        json.put("name", text);
        json.put("token", getToken());

        String result = HttpRequest.post("http://ej666.com/v/api/getTTZJB").
                header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Accept", "*/*")
                .header("Accept-Language", "zh-CN,zh-Hans;q=0.9")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Host", "ej666.com")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.2 Safari/605.1.15")
                .header("Connection", "keep-alive")
                .header("Cookie", "Hm_lpvt_0606cc863fe63b0953d06585b4897bcb=1671510857; Hm_lvt_0606cc863fe63b0953d06585b4897bcb=1669378804,1670119326,1670130069,1671364887")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Origin", "http://ej666.com")

                .body(json.toString())
                .execute().body();


        return result;
    }


    private static String getXiaoy(String text) {
        HashMap<String, String> headMap = new HashMap<>();
        headMap.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        headMap.put("Accept", "*/*");
        headMap.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148");
        headMap.put("Accept-Language", "zh-CN,zh-Hans;q=0.9");
        headMap.put("Accept-Encoding", "gzip, deflate");
        headMap.put("Connection", "keep-alive");
        // headMap.put("Host", "175.178.114.175:8090");
        HashMap<String, Object> bodyMap = new HashMap<>();
        JSONObject json = new JSONObject();
        json.put("name", text);
        json.put("token", getToken());
        String result = HttpRequest.post("http://ej666.com/v/api/getXiaoy").
                header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Accept", "*/*")
                .header("Accept-Language", "zh-CN,zh-Hans;q=0.9")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Host", "ej666.com")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.2 Safari/605.1.15")
                .header("Connection", "keep-alive")
                .header("Cookie", "Hm_lpvt_0606cc863fe63b0953d06585b4897bcb=1671510857; Hm_lvt_0606cc863fe63b0953d06585b4897bcb=1669378804,1670119326,1670130069,1671364887")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Origin", "http://ej666.com")

                .body(json.toString())
                .execute().body();

        return result;
    }

    private static String getJuzi(String text) {
        HashMap<String, String> headMap = new HashMap<>();
        headMap.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        headMap.put("Accept", "*/*");
        headMap.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148");
        headMap.put("Accept-Language", "zh-CN,zh-Hans;q=0.9");
        headMap.put("Accept-Encoding", "gzip, deflate");
        headMap.put("Connection", "keep-alive");
        // headMap.put("Host", "175.178.114.175:8090");
        HashMap<String, Object> bodyMap = new HashMap<>();
        JSONObject json = new JSONObject();
        json.put("name", text);
        json.put("token", getToken());
        String result = HttpRequest.post("http://ej666.com/v/api/getJuzi").
                header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Accept", "*/*")
                .header("Accept-Language", "zh-CN,zh-Hans;q=0.9")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Host", "ej666.com")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.2 Safari/605.1.15")
                .header("Connection", "keep-alive")
                .header("Cookie", "Hm_lpvt_0606cc863fe63b0953d06585b4897bcb=1671510857; Hm_lvt_0606cc863fe63b0953d06585b4897bcb=1669378804,1670119326,1670130069,1671364887")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Origin", "http://ej666.com")

                .body(json.toString())
                .execute().body();

        return result;
    }


    private static String getToken() {
        String url = "http://ej666.com/v/api/gettoken";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        paramMap.put("Accept", "*/*");
        paramMap.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148");
        paramMap.put("Accept-Language", "zh-CN,zh-Hans;q=0.9");
        paramMap.put("Accept-Encoding", "gzip, deflate");
        paramMap.put("Connection", "keep-alive");
        paramMap.put("Host", "ej666.com");
        paramMap.put("If-None-Match", "W/\"31-SrKlhagvo4BY+hHIpBjJuJJS3l8\"");
        paramMap.put("Cookie", "Hm_lpvt_0606cc863fe63b0953d06585b4897bcb=1671511848; Hm_lvt_0606cc863fe63b0953d06585b4897bcb=1669378804,1670119326,1670130069,1671364887");
        String result = HttpUtil.get(url, paramMap);
        return JSONUtil.parseObj(result).getStr("token");
    }


    public static String getDyfx(String text) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("name", text)
                .add("token", "i69")
                .build();

        Request request = new Request.Builder()
                .url("http://mx559.cn/v/api/getDyfx")
                .post(formBody)
                .header("Host", "mx559.cn")
                .header("Accept", "*/*")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Accept-Language", "zh-CN,zh-Hans;q=0.9")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Origin", "http://mx559.cn")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.5 Safari/605.1.15")
                .header("Cookie", "Hm_lpvt_cce07f87930c14786d9eced9c08d0e89=1686987785; Hm_lvt_cce07f87930c14786d9eced9c08d0e89=1686316237,1686399278,1686615659,1686987505")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
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


        System.out.println(messageTest("破毒抢人"));
    }

}
