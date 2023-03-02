package com.github.binarywang.demo.wx.mp.handler;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.binarywang.demo.wx.mp.builder.TextBuilder;
import com.github.binarywang.demo.wx.mp.entity.JsonsRootBean;
import com.github.binarywang.demo.wx.mp.entity.Lists;
import com.github.binarywang.demo.wx.mp.task.TaskRunner;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Component
public class MsgHandler extends AbstractHandler {
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地
        }

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


        if (wxMessage.getFromUser().equals("oDVX56e9DU6GqAKNwJ9xsU9axKFs") || wxMessage.getFromUser().equals("oDVX56bDrhCpo1Ox1bbq6DAGuXJ4")) {
            TaskRunner t = new TaskRunner();

            String msg = wxMessage.getContent().replace("-", " ");
            String[] split = msg.split("\\\n");
            ArrayList<int[]> objects = new ArrayList<>();

            for (String s : split) {

                int[] s1 = Arrays.asList(s.split(" ")).stream().mapToInt(Integer::parseInt).toArray();
                objects.add(s1);
            }
            try {
                t.run(objects);

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        //TODO 组装回复消息
        String content = analysisJson(get2(wxMessage.getContent()));
        if (content.length() < 1) {
            content = "没查询到相关内容";
        }

        return new TextBuilder().build(content, wxMessage, weixinService);

    }


    private static String get(String text) {
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
        String result = HttpRequest.post(" http://175.178.114.175:8090/api/getTTZJB").
                header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .header("Accept", "*/*")
                .header("Accept-Language", "zh-CN,zh-Hans;q=0.9")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Host", "175.178.114.175:8090")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.2 Safari/605.1.15")
                .header("Connection", "keep-alive")
                //.header("X-Requested-With:", "XMLHttpRequest")
                .body(json.toString()).execute().body();


        return result;
    }


    private static String getToken() {
        String url = "http://175.178.114.175:8090/api/gettoken";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        paramMap.put("Accept", "*/*");
        paramMap.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148");
        paramMap.put("Accept-Language", "zh-CN,zh-Hans;q=0.9");
        paramMap.put("Accept-Encoding", "gzip, deflate");
        paramMap.put("Connection", "keep-alive");
        paramMap.put("Host", "175.178.114.175:8090");
        paramMap.put("If-None-Match", "W/\"2b-d1crqQhfysq0OmXmoZsMjYWvRXg\"");
        String result = HttpUtil.get(url, paramMap);
        return JSONUtil.parseObj(result).getStr("token");
    }


    private static String get2(String text) {
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
        json.put("token", getToken2());
        String result = HttpRequest.post("http://api.kkkob.com/v/api/getJuzi").
                header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Accept", "*/*")
                .header("Accept-Language", "zh-CN,zh-Hans;q=0.9")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Host", "api.kkkob.com")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.2 Safari/605.1.15")
                .header("Connection", "keep-alive")
                .header("Cookie", "Hm_lpvt_0606cc863fe63b0953d06585b4897bcb=1671510857; Hm_lvt_0606cc863fe63b0953d06585b4897bcb=1669378804,1670119326,1670130069,1671364887")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Origin", "http://npcls.com")

                .body(json.toString())
                .execute().body();


        return result;
    }


    private static String getToken2() {
        String url = "http://api.kkkob.com/v/api/gettoken";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        paramMap.put("Accept", "*/*");
        paramMap.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148");
        paramMap.put("Accept-Language", "zh-CN,zh-Hans;q=0.9");
        paramMap.put("Accept-Encoding", "gzip, deflate");
        paramMap.put("Connection", "keep-alive");
        paramMap.put("Host", "npcls.com");
        paramMap.put("If-None-Match", "W/\"31-SrKlhagvo4BY+hHIpBjJuJJS3l8\"");
        paramMap.put("Cookie", "Hm_lpvt_0606cc863fe63b0953d06585b4897bcb=1671511848; Hm_lvt_0606cc863fe63b0953d06585b4897bcb=1669378804,1670119326,1670130069,1671364887");
        String result = HttpUtil.get(url, paramMap);
        return JSONUtil.parseObj(result).getStr("token");
    }


    public static String analysisJson(String json) {


        if (json.length() > 0) {
            JsonsRootBean jsonsRootBean = JSONUtil.toBean(json, JsonsRootBean.class);
            StringBuffer sb = new StringBuffer();
            for (Lists lists : jsonsRootBean.getList()) {
                sb.append(lists.toString() + "\r\n");

            }
            return sb.toString();
        }
        return "暂无资源";
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String msg = "3 15 16 21 26 32-5\n1 14 16 22 26 31-12 \n4 13 17 27 29 30-6 \n4 9 11 13 27 28-11\n7 11 20 23 25 32-1";


    }
}
