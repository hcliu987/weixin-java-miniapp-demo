package com.hc.wx.mp;

import com.hc.wx.mp.config.WxMpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@SpringBootApplication
@EnableScheduling
@Slf4j
public class WxMpDemoApplication {



    public static void main(String[] args) throws UnknownHostException {

        ConfigurableApplicationContext run = SpringApplication.run(WxMpDemoApplication.class, args);

    }

}
