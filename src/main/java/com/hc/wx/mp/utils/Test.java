package com.hc.wx.mp.utils;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

public class Test {
    public static void main(String[] args) throws IOException  {
        URL url = new URL("https://gw.huiqunchina.com/front-manager/api/customer/promotion/checkCustomerInQianggou");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Host", "gw.huiqunchina.com");
        httpConn.setRequestProperty("Cookie", "JSESSIONID=BE3458881D16335E41DA116E0C7E5A02; acw_tc=2f63148216900952785656399e7f78506fdf362fd8b2c18783be8f24dff229; aliyungf_tc=268df77c773742a97b5f3260afe976e69a8cae065a9629d8e17d4e2ebd8b986e");
        httpConn.setRequestProperty("X-HMAC-Date", "Sun, 23 Jul 2023 06:54:56 GMT");
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 16_5_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.39(0x18002732) NetType/WIFI Language/zh_CN miniProgram/wx5508e31ffe9366b8");
        httpConn.setRequestProperty("Referer", "https://hqmall.huiqunchina.com/");
        httpConn.setRequestProperty("dataType", "json");
        httpConn.setRequestProperty("X-access-token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDEzMTE1OTA4IiwiaXNzIjoiZ21hbGwtc3RhcnNreSIsImxvZ0lkIjoibnVsbCIsImV4cCI6MTY5NzQxODk0NiwiaWF0IjoxNjg5NjQyOTQ2fQ.E94K1cPDgyY0lbUqN9UBP7YVs2aF1P1F3rZJHRUOZPI");
        httpConn.setRequestProperty("Channel", "miniapp");
        httpConn.setRequestProperty("Origin", "https://hqmall.huiqunchina.com");
        httpConn.setRequestProperty("Sec-Fetch-Dest", "empty");
        httpConn.setRequestProperty("X-HMAC-DIGEST", "FdKK7v8pOW+1ygURaGKZeFw3SVLxYiyHw3ectN9lzGQ=");
        httpConn.setRequestProperty("Sec-Fetch-Site", "same-site");
        httpConn.setRequestProperty("Content-Length", "32");
        httpConn.setRequestProperty("Connection", "keep-alive");
        httpConn.setRequestProperty("Accept-Language", "zh-CN,zh-Hans;q=0.9");
        httpConn.setRequestProperty("X-HMAC-ALGORITHM", "hmac-sha256");
        httpConn.setRequestProperty("X-HMAC-SIGNATURE", "Nl39XWb5ErMY6LLfs8iHfuGI2zBymouyqeESxNGHiB8=");
        httpConn.setRequestProperty("X-HMAC-ACCESS-KEY", "c1ea3cdb3b2043cff6c166fe62d2eee4");
        httpConn.setRequestProperty("Accept", "*/*");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        httpConn.setRequestProperty("Sec-Fetch-Mode", "cors");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write("{\"activityId\":303,\"channelId\":3}");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        if ("gzip".equals(httpConn.getContentEncoding())) {
            responseStream = new GZIPInputStream(responseStream);
        }
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        System.out.println(response);
    }

}
