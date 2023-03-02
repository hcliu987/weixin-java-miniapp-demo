package com.github.binarywang.demo.wx.mp.task;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class TaskRunner {


    public static List myNumbers = new ArrayList<int[]>();
    public static int count = 0;
    public static int money = 0;


   // @Scheduled(cron = "10 41 22 ? * 2,4,7")
    public void run(ArrayList<int[]> myNumbers) throws UnsupportedEncodingException {
        int redNumbers = 0;
        int blueNumbers = 0;
        String[] luckyNumbers = getLastSsq();
        List<Integer> resut = new ArrayList();
        //判断红球中了几个
        int[] userNumbers = null;
        for (int x = 0; x < myNumbers.size(); x++) {

            userNumbers = (int[]) myNumbers.get(x);
            for (int i = 0; i < userNumbers.length - 1; i++) {
                for (int j = 0; j < luckyNumbers.length - 1; j++) {
                    if (userNumbers[i] == Integer.valueOf(luckyNumbers[j])) {
                        resut.add(userNumbers[i]);
                        redNumbers++;
                        break;
                    }

                }

            }
            System.out.println("中奖号码是：");
            printArray(luckyNumbers);
            System.out.println("您投注的号码是：");
            printArray(userNumbers);

            System.out.println("您中了" + redNumbers + "个红球,他们分别是:" + resut.toString());
            //判断蓝球是否命中
            blueNumbers = userNumbers[6] == Integer.valueOf(luckyNumbers[6]) ? 1 : 0;
            System.out.println("您是否命中蓝球:" + (blueNumbers == 1 ? "是" : "否"));

            if (blueNumbers == 1 && redNumbers < 3) {
                money += 05;
                count += 1;
                System.err.println("恭喜你中了六等奖");
            } else if (blueNumbers == 1 && redNumbers == 3 || blueNumbers == 0 && redNumbers == 4) {
                money += 10;
                count += 1;
                System.err.println("恭喜你中了五等奖");
            } else if (blueNumbers == 1 && redNumbers == 4 || blueNumbers == 0 && redNumbers == 5) {
                System.err.println("恭喜你中了四等奖");
                money += 200;
                count += 1;
            } else if (blueNumbers == 1 && redNumbers == 5) {
                System.err.println("恭喜你中了三等奖");
                money += 300;
                count += 1;
            } else if (blueNumbers == 0 && redNumbers == 6) {
                System.err.println("恭喜你中了二等奖");
                money += 100000;
                count += 1;
            } else if (blueNumbers == 1 && redNumbers == 6) {
                money += 1000000;
                count += 1;

                System.err.println("恭喜你中了一等奖");
                String url = "https://api.day.app/VizApLTywWLsn4eUjHADRC/" + "你暴富了兄弟";
            } else {
                System.err.println("您未中奖，感谢你为福利事业做出贡献!!!");
            }
            redNumbers = 0;
            blueNumbers = 0;
            resut.clear();

            System.out.println("中奖总金额" + money);
            System.out.println("中奖数" + count);

        }
        String url = "https://api.day.app/VizApLTywWLsn4eUjHADRC/" + "本期福利全色球号码" + Arrays.toString(luckyNumbers) + ":中奖" + count + "组,中奖金额：" + money;
        String serverUrl = "https://sctapi.ftqq.com/SCT142384Tq64Jxx4Jde2xQDQjct36FD4Z.send";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 设置请求参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("text", "中奖通知");
        String desp = "本期福利全色球号码" + Arrays.toString(luckyNumbers) + ":中奖" + count + "组,中奖金额：" + money;
        params.add("desp", desp);
        HttpUtil.get(url);

        new RestTemplate().exchange(serverUrl, org.springframework.http.HttpMethod.POST, new HttpEntity<>(params, headers), String.class);
    }


    public static void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(i == arr.length - 1 ? arr[i] : arr[i] + ", ");
        }
        System.out.println("]");
    }

    public static void printArray(String[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(i == arr.length - 1 ? arr[i] : arr[i] + ", ");
        }
        System.out.println("]");
    }


    /**
     * 获取最新一期双色球
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public String[] getLastSsq() throws UnsupportedEncodingException {
        String ssq = null;
        String url = "http://www.cwl.gov.cn/cwl_admin/front/cwlkj/search/kjxx/findDrawNotice?name=ssq&issueCount=1";
        Map<String, String> head = new HashMap<String, String>();
        head.put("Cookie", "HMF_CI=a76a1a17a9c2371ed880d725c7a2ab738aafecc050ff110f043996a09c1e51355937253876c71a185e38e5be7cfe2d066db3358a6b2190a00ece7f9373cff6fd57; 21_vq=3");
        String result = HttpURLConnectionUtil.doGet(url, "utf-8", head, null);
        if (StringUtils.isNotBlank(result)) {
            ResultBean resultBean = JSONUtil.toBean(result, ResultBean.class);
            System.out.println("最新一期双色球结果 :" + resultBean.toString());
            SsqVo ssqVo = resultBean.getResult().get(0);
            ssq = ssqVo.getRed() + "," + ssqVo.getBlue();
        }
        return StringUtils.split(ssq, ",");
    }


}

class HttpURLConnectionUtil {

    public static String doGet(String url, String charset, Map<String, String> head, Map<String, String> parmas) throws UnsupportedEncodingException {
        //1.生成HttpClient对象并设置参数
        HttpClient httpClient = new HttpClient();
        //设置Http连接超时为5秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        //2.生成GetMethod对象并设置参数
        GetMethod getMethod = new GetMethod(url);
        //设置get请求超时为5秒
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        //head
        Iterator<String> iterator1 = head.keySet().iterator();
        String key1 = null;
        while (iterator1.hasNext()) {
            key1 = iterator1.next();
            getMethod.addRequestHeader(key1, head.get(key1));
        }
        //parmas
        if (parmas != null && parmas.size() > 0) {
            Iterator<String> iterator = parmas.keySet().iterator();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                getMethod.getParams().setParameter(key, parmas.get(key));
            }
        }
        //设置请求重试处理，用的是默认的重试处理：请求三次
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        String response = "";
        //3.执行HTTP GET 请求
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            //4.判断访问的状态码
            if (statusCode != HttpStatus.SC_OK) {
            }
            //5.处理HTTP响应内容
            //HTTP响应头部信息，这里简单打印
            Header[] headers = getMethod.getResponseHeaders();
            //读取HTTP响应内容，这里简单打印网页内容
            //读取为字节数组
            byte[] responseBody = getMethod.getResponseBody();
            response = new String(responseBody, charset);
            //读取为InputStream，在网页内容数据量大时候推荐使用
            //InputStream response = getMethod.getResponseBodyAsStream();
        } catch (HttpException e) {
            //发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("请检查输入的URL!");
            e.printStackTrace();
        } catch (IOException e) {
            //发生网络异常
            System.out.println("发生网络异常!");
        } finally {
            //6.释放连接
            getMethod.releaseConnection();
        }
        return response;
    }
}

@Data
class ResultBean {
    private String state;
    private String message;
    private String Tflag;
    private List<SsqVo> result;
}

@Data
class SsqVo {

    private String name;

    private String code;

    private String red;

    private String blue;
}
