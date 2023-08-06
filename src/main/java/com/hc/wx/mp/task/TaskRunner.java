package com.hc.wx.mp.task;


import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hc.wx.mp.config.LotteryProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


@Component
@Slf4j
public class TaskRunner {


    public static int count = 0;
    public static int money = 0;


    // @Scheduled(cron = "10 41 22 ? * 2,4,7")
    public void run(List<int[]> Numbers, String name) throws UnsupportedEncodingException {
        String uToken = "";
        List myNumbers = Numbers;

        uToken = "WXbF6u6v6iKfPyVD5ttCgK";


        int redNumbers = 0;
        int blueNumbers = 0;
        String[] luckyNumbers = getLastSsq();
        //判断红球中了几个
        int[] userNumbers = null;
        //中奖号码
        List<Integer> zjNumers = null;
        for (int x = 0; x < myNumbers.size(); x++) {

            userNumbers = (int[]) myNumbers.get(x);

            printArray(userNumbers);
            for (int i = 0; i < userNumbers.length - 1; i++) {
                for (int j = 0; j < luckyNumbers.length - 1; j++) {
                    if (userNumbers[i] == Integer.valueOf(luckyNumbers[j])) {
                        System.out.println(userNumbers[i]);
                        redNumbers++;

                        zjNumers = new ArrayList<>();
                        zjNumers.add(Integer.valueOf(luckyNumbers[j]));
                        break;
                    }

                }
            }
            System.out.println("中奖号码是：");
            printArray(luckyNumbers);
            System.out.println("您投注的号码是：");
            printArray(userNumbers);


            blueNumbers = userNumbers[6] == Integer.valueOf(luckyNumbers[6]) ? 1 : 0;

            int blues = userNumbers[6] == Integer.valueOf(luckyNumbers[6]) ? userNumbers[6] : 0;
            int[] array = null;
            if (zjNumers == null || zjNumers.size() < 1) {
                array = new int[]{};
            } else {
                array = zjNumers.stream().mapToInt(Integer::valueOf).toArray();
            }

            array = IntStream.concat(Arrays.stream(array), Arrays.stream(new int[]{blues})).toArray();
            if (array[0] != 0) {
                System.out.println("我投资中奖的号码: ");
                printArray(array);
            }
            if (blueNumbers == 1 && redNumbers < 3) {
                money += 5;
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
            if (money > 0) {
                notice(money, luckyNumbers, array, uToken);
            } else {
                String url = "https://api.day.app/" + uToken + "/本期福利双色球号码" + Arrays.toString(luckyNumbers) + "未中奖";
                String serverUrl = "https://sctapi.ftqq.com/SCT142384Tq64Jxx4Jde2xQDQjct36FD4Z.send";
                String pushdeerUrl = "https://api2.pushdeer.com/message/push?pushkey=PDU21229TiqefGW5MIxU3C69S5KhM5efHWhtKpCUP&text=" + "/本期福利双色球号码" + Arrays.toString(luckyNumbers) + "未中奖";
                ;
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                // 设置请求参数
                Map<String, Object> params = new HashMap<>();
                params.put("text", "中奖通知");
                String desp = "本期福利双色球号码" + Arrays.toString(luckyNumbers) + "未中奖";
                params.put("desp", desp);
                HttpUtil.get(url);
                log.info(url + "调用成功过");
                HttpUtil.get(pushdeerUrl);
                log.info(pushdeerUrl + "调用成功");
                String pushPlus = "http://www.pushplus.plus/send?token=00e469292f6e4bee87b718d578417329&title=" + "/本期福利双色球号码" + "&content=" + Arrays.toString(luckyNumbers) + "未中奖";

                HttpUtil.post(serverUrl, params);
                HttpUtil.get(pushPlus);
            }
            redNumbers = 0;
            blueNumbers = 0;
            userNumbers = null;
            zjNumers = null;
            array = null;
            money = 0;

        }


    }

    public void notice(int money, String[] lastSsq, int[] zjNumber, String uToken) {
        String url = "https://api.day.app/" + uToken + "/本期福利双色球号码" + Arrays.toString(lastSsq) + ":中奖号码:" + Arrays.toString(zjNumber) + ",中奖金额：" + money;
        String serverUrl = "https://sctapi.ftqq.com/SCT142384Tq64Jxx4Jde2xQDQjct36FD4Z.send";
        String pushdeerUrl = "https://api2.pushdeer.com/message/push?pushkey=PDU21229TiqefGW5MIxU3C69S5KhM5efHWhtKpCUP&text=" + "/本期福利双色球号码" + Arrays.toString(lastSsq) + ":中奖号码：" + Arrays.toString(zjNumber) + " 中奖金额：" + money;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 设置请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("text", "中奖通知");
        String desp = "本期福利双色球号码" + Arrays.toString(lastSsq) + ":中奖号码:" + Arrays.toString(zjNumber) + ",中奖金额：" + money;
        params.put("desp", desp);
        HttpUtil.get(url);
        log.info("\n调用通知信息：[{}]", url);
        HttpUtil.get(pushdeerUrl);
        log.info("\n调用通知信息：[{}]", pushdeerUrl);
        String pushPlus = "http://www.pushplus.plus/send?token=00e469292f6e4bee87b718d578417329&title=" + "/本期福利双色球号码" + "&content=" + Arrays.toString(lastSsq) + ":中奖号码:" + Arrays.toString(zjNumber) + ",中奖金额：" + money;

        HttpUtil.post(serverUrl, params);
        log.info("\n调用通知信息：[{}, {}]", serverUrl, params);
        HttpUtil.get(pushPlus);
        log.info("\n调用通知信息：[{}]", pushPlus);
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
        System.out.println(result);
        if (StringUtils.isNotBlank(result)) {
            ResultBean resultBean = JSONUtil.toBean(result, ResultBean.class);

            System.out.println("最新一期双色球结果 :" + resultBean.toString());
            SsqVo ssqVo = resultBean.getResult().get(0);
            ssq = ssqVo.getRed() + "," + ssqVo.getBlue();
        }
        return StringUtils.split(ssq, ",");
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

    }


    static  class HttpURLConnectionUtil {

        public  static  String  doGet(String url, String charset, Map<String, String> head, Map<String, String> parmas) throws UnsupportedEncodingException {
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
}