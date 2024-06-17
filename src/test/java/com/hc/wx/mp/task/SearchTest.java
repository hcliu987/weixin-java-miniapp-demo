package com.hc.wx.mp.task;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.hc.wx.mp.service.SearchService;
import okhttp3.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SearchTest {

    @Autowired
    private SearchService service;
    @Autowired
    private Scheduler scheduler;

    private String text = "{\"list\":[{\"question\":\"看不见影子的少年2024（夸克）\",\"answer\":\"看不见影子的少年2024（夸克）链接：https://pan.quark.cn/s/ad1c8cf97e4d?entry=sjss\"},{\"question\":\"看不见影子的少年2024（迅雷）\",\"answer\":\"看不见影子的少年2024（迅雷）链接：https://pan.xunlei.com/s/VO-L186kF1Gwg913jTsYFZnaA1?pwd=9gt5&origin=lilizj# 提取码：9gt5\"},{\"question\":\"看不见影子的少年2024\",\"answer\":\"看不见影子的少年2024链接：https://pan.baidu.com/s/1FzLUfugfKWvu-mpcw6Y_oQ?pwd=wr7z 提取码：wr7z\"}],\"us\":true,\"msg\":\"ok\",\"cache\":true}";
    private String txt = "{\"us\":true,\"list\":[{\"id\":70323,\"question\":\"[看不见影子的少年][2024][全16集][国产剧]\",\"answer\":\"链接：https://pan.baidu.com/s/1XNuFl-Hv0WGDbvnqQf69qQ?pwd=8888 提取码：8888\\n链接：https://pan.xunlei.com/s/VO-U_LT4A2YMWyDmqIx9pQreA1\\n链接：https://pan.quark.cn/s/f797b484f204\",\"isTop\":0}],\"msg\":\"ok\"}";

    @Test
    public void test() throws Exception {
//        System.out.println(service.search("看不见影子的少年"));
//        System.out.println(service.getJuzi("看不见影子的少年"));
//        System.out.println(service.getXiaoyu("看不见影子的少年"));
      //   System.out.println(service.resultMsg("看不见影子的少年"));
        System.out.println(service.resultMsg("看不见影子的少年").length());
    }

//        String msg = service.resultMsgAll("看不见影子的少年");
//        System.out.println("当前结果：" + msg);

}




